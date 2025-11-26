import { Modal, Table, Tag } from "antd";
import dayjs from "dayjs";

/**
 * Format datetime theo Rule 15: dd/MM/yyyy HH:mm
 */
const formatDate = (value) => {
  if (!value) return "";
  return dayjs(value).format("DD/MM/YYYY HH:mm");
};

/**
 * Modal hiển thị chi tiết hóa đơn + payment
 */
export default function InvoiceDetailModal({ open, onClose, data }) {
  if (!data) return null;

  const items = data.items || [];
  const payment = data.payment; // ⭐ lấy thông tin thanh toán

  return (
    <Modal
      open={open}
      onCancel={onClose}
      footer={null}
      width={750}
      title={`Chi tiết hóa đơn ${data.code}`}
    >
      {/* ================================
          THÔNG TIN CƠ BẢN
      ================================= */}
      <p>
        <b>Mã hóa đơn:</b> {data.code}
      </p>
      <p>
        <b>Mã đơn hàng:</b> {data.orderId}
      </p>
      <p>
        <b>Người tạo:</b> {data.createdBy}
      </p>
      <p>
        <b>Ngày tạo:</b> {formatDate(data.createdAt)}
      </p>

      {/* ================================
          PAYMENT SECTION
      ================================= */}
      <div style={{ marginTop: 20, marginBottom: 10 }}>
        <h3>Thông tin thanh toán</h3>

        {payment ? (
          <div
            style={{
              padding: 12,
              border: "1px solid #eee",
              borderRadius: 8,
              background: "#fafafa",
            }}
          >
            <p>
              <b>Phương thức:</b> {payment.method}
            </p>

            <p>
              <b>Số tiền:</b>{" "}
              {payment.amount?.toLocaleString()} đ
            </p>

            <p>
              <b>Trạng thái:</b>{" "}
              <Tag color={payment.status === "SUCCESS" ? "green" : "red"}>
                {payment.status}
              </Tag>
            </p>

            <p>
              <b>Mã giao dịch:</b> {payment.transactionCode || "—"}
            </p>

            <p>
              <b>Thanh toán lúc:</b> {formatDate(payment.paidAt)}
            </p>
          </div>
        ) : (
          <Tag color="red">Chưa thanh toán</Tag>
        )}
      </div>

      {/* ================================
          DANH SÁCH MÓN
      ================================= */}
      <h3>Danh sách món</h3>
      <Table
        dataSource={items}
        rowKey="id"
        pagination={false}
        columns={[
          {
            title: "Món ăn",
            dataIndex: "name",
          },
          {
            title: "SL",
            dataIndex: "quantity",
          },
          {
            title: "Giá",
            dataIndex: "price",
            render: (v) => `${v?.toLocaleString()} đ`,
          },
          {
            title: "Thành tiền",
            dataIndex: "amount",
            render: (v) => `${v?.toLocaleString()} đ`,
          },
        ]}
        style={{ marginBottom: 20 }}
      />

      {/* ================================
          TỔNG TIỀN
      ================================= */}
      <div style={{ textAlign: "right", fontSize: 16 }}>
        <b>Tổng tiền: {data.totalAmount?.toLocaleString()} đ</b>
      </div>
    </Modal>
  );
}
