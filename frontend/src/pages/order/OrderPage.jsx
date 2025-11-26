import { useEffect, useState } from "react";
import { Table, Button, Modal, message, Popconfirm } from "antd";
import dayjs from "dayjs";

import {
  getOrders,
  getOrderById, // Lấy chi tiết đơn để xem modal
  createOrder,
  updateOrder,
  deleteOrder,
  payOrder, // API thanh toán đơn hàng
} from "../../api/orderApi";

import { getDishes } from "../../api/dishApi";

import OrderForm from "../../components/order/OrderForm";
import InvoiceDetailModal from "../../components/invoice/InvoiceDetailModal";

/**
 * Trang quản lý đơn hàng
 * - Hiển thị danh sách đơn
 * - Thêm / sửa / xóa
 * - Thanh toán đơn → tự động tạo hóa đơn → mở modal xem hóa đơn
 */
export default function OrderPage() {
  // State dữ liệu
  const [orders, setOrders] = useState([]);
  const [dishes, setDishes] = useState([]);

  // Modal thêm / sửa đơn
  const [openModal, setOpenModal] = useState(false);
  const [editingOrder, setEditingOrder] = useState(null);

  // Modal xem chi tiết đơn
  const [detailModalOpen, setDetailModalOpen] = useState(false);
  const [detailOrder, setDetailOrder] = useState(null);

  // Modal hóa đơn sau khi thanh toán
  const [invoiceModalOpen, setInvoiceModalOpen] = useState(false);
  const [currentInvoice, setCurrentInvoice] = useState(null);

  /**
   * Load danh sách đơn hàng
   */
  const loadOrders = async () => {
    try {
      const data = await getOrders();
      setOrders(data);
    } catch (err) {
      message.error("Không tải được danh sách đơn hàng");
    }
  };

  /**
   * Load danh sách món ăn (để dùng trong OrderForm)
   */
  const loadDishes = async () => {
    try {
      const res = await getDishes();
      // Rule 19: chuẩn hóa cho dish API
      setDishes(res.data || res);
    } catch (err) {
      message.error("Không tải được danh sách món");
    }
  };

  /**
   * Xem chi tiết 1 đơn hàng
   */
  const handleViewDetail = async (id) => {
    try {
      const data = await getOrderById(id);
      setDetailOrder(data);
      setDetailModalOpen(true);
    } catch (err) {
      message.error("Không tải được chi tiết đơn hàng");
    }
  };

  useEffect(() => {
    loadOrders();
    loadDishes();
  }, []);

  /**
   * Mở form thêm
   */
  const handleAdd = () => {
    setEditingOrder(null);
    setOpenModal(true);
  };

  /**
   * Mở form sửa
   */
  const handleEdit = (record) => {
    setEditingOrder({
      ...record,
      items: record.items.map((i) => ({
        dishId: i.dishId,
        quantity: i.quantity,
      })),
    });
    setOpenModal(true);
  };

  /**
   * Xóa đơn hàng
   */
  const handleDelete = async (id) => {
    try {
      await deleteOrder(id);
      message.success("Đã xóa đơn hàng");
      loadOrders();
    } catch (err) {
      message.error("Xóa thất bại");
    }
  };

  /**
   * Thanh toán đơn hàng:
   * - Confirm
   * - Gọi API payOrder → backend tạo hóa đơn
   * - Mở modal xem hóa đơn
   * - Reload lại danh sách
   */
  const handlePayOrder = (orderId) => {
    Modal.confirm({
      title: "Xác nhận thanh toán",
      content: "Bạn có chắc muốn thanh toán đơn hàng này và tạo hóa đơn không?",
      okText: "Thanh toán",
      okType: "primary",
      cancelText: "Hủy",
      onOk: async () => {
        try {
          const invoice = await payOrder(orderId); // trả về InvoiceResponse
          message.success("Thanh toán thành công, hóa đơn đã được tạo!");

          setCurrentInvoice(invoice); // lưu dữ liệu hóa đơn
          setInvoiceModalOpen(true);  // mở modal hóa đơn

          loadOrders(); // reload danh sách đơn hàng
        } catch (error) {
          console.error(error);
          message.error("Thanh toán thất bại, vui lòng thử lại!");
        }
      },
    });
  };

  /**
   * Lưu/submit đơn hàng (thêm hoặc sửa)
   */
  const handleSubmit = async (values) => {
    try {
      if (editingOrder) {
        await updateOrder(editingOrder.id, values);
        message.success("Cập nhật đơn thành công");
      } else {
        await createOrder(values);
        message.success("Tạo đơn thành công");
      }

      setOpenModal(false);
      loadOrders();
    } catch (err) {
      message.error("Lỗi khi lưu đơn hàng");
    }
  };

  /**
   * Cột hiển thị table đơn hàng
   */
  const columns = [
    {
      title: "Mã đơn",
      dataIndex: "code",
      render: (value, record) => (
        <a onClick={() => handleViewDetail(record.id)}>{value}</a>
      ),
    },
    {
      title: "Số bàn",
      dataIndex: "tableNumber",
    },
    {
      title: "Khách hàng",
      dataIndex: "customerName",
    },
    {
      title: "Tổng tiền",
      dataIndex: "totalAmount",
      render: (v) => `${v.toLocaleString()} đ`,
    },
    {
      title: "Ngày tạo",
      dataIndex: "createdAt",
      render: (v) => dayjs(v).format("DD/MM/YYYY HH:mm"),
    },
    {
      title: "Hành động",
      render: (_, record) => (
        <>
          <Button
            type="primary"
            onClick={() => handleEdit(record)}
            style={{ marginRight: 8 }}
          >
            Sửa
          </Button>

          <Popconfirm
            title="Xóa đơn?"
            onConfirm={() => handleDelete(record.id)}
            okText="Xóa"
            cancelText="Hủy"
          >
            <Button danger style={{ marginRight: 8 }}>Xóa</Button>
          </Popconfirm>

          {/* Chỉ hiện nút thanh toán nếu đơn chưa PAID */}
          {record.status !== "PAID" && (
            <Button
              style={{
                background: "#52c41a",
                color: "white",
                borderColor: "#52c41a",
              }}
              onClick={() => handlePayOrder(record.id)}
            >
              Thanh toán
            </Button>
          )}
        </>
      ),
    },
  ];

  return (
    <div>
      <h2>Quản lý đơn hàng</h2>

      {/* nút thêm đơn */}
      <Button type="primary" onClick={handleAdd} style={{ marginBottom: 16 }}>
        + Thêm đơn
      </Button>

      {/* bảng danh sách đơn */}
      <Table rowKey="id" columns={columns} dataSource={orders} />

      {/* Modal thêm/sửa đơn */}
      <Modal
        open={openModal}
        title={editingOrder ? "Sửa đơn hàng" : "Thêm đơn hàng"}
        onCancel={() => setOpenModal(false)}
        footer={null}
        width={700}
      >
        <OrderForm
          dishes={dishes}
          initialValues={editingOrder}
          onSubmit={handleSubmit}
        />
      </Modal>

      {/* Modal xem chi tiết đơn */}
      <Modal
        open={detailModalOpen}
        title="Chi tiết đơn hàng"
        onCancel={() => setDetailModalOpen(false)}
        footer={[
          <Button key="close" onClick={() => setDetailModalOpen(false)}>
            Đóng
          </Button>,
        ]}
        width={700}
      >
        {detailOrder && (
          <div>
            <p>
              <b>Mã đơn:</b> {detailOrder.code}
            </p>
            <p>
              <b>Tên khách:</b> {detailOrder.customerName}
            </p>
            <p>
              <b>SĐT:</b> {detailOrder.customerPhone}
            </p>
            <p>
              <b>Số bàn:</b> {detailOrder.tableNumber}
            </p>
            <p>
              <b>Ghi chú:</b> {detailOrder.note}
            </p>
            <p>
              <b>Ngày tạo:</b>{" "}
              {dayjs(detailOrder.createdAt).format("DD/MM/YYYY HH:mm")}
            </p>

            <h4 style={{ marginTop: 20 }}>Danh sách món</h4>

            <Table
              dataSource={detailOrder.items}
              rowKey="id"
              pagination={false}
              columns={[
                {
                  title: "Món ăn",
                  dataIndex: "dishName",
                },
                {
                  title: "SL",
                  dataIndex: "quantity",
                },
                {
                  title: "Giá",
                  dataIndex: "price",
                  render: (v) => `${v.toLocaleString()} đ`,
                },
                {
                  title: "Thành tiền",
                  dataIndex: "amount",
                  render: (v) => `${v.toLocaleString()} đ`,
                },
              ]}
            />

            <div
              style={{ marginTop: 20, textAlign: "right", fontSize: 16 }}
            >
              <b>
                Tổng tiền: {detailOrder.totalAmount.toLocaleString()} đ
              </b>
            </div>
          </div>
        )}
      </Modal>

      {/* 🌟 Modal hiển thị hóa đơn sau khi thanh toán (LEVEL ROOT – không nằm trong modal khác) */}
      {invoiceModalOpen && (
        <InvoiceDetailModal
          open={invoiceModalOpen}
          onClose={() => setInvoiceModalOpen(false)}
          data={currentInvoice}
        />
      )}
    </div>
  );
}
