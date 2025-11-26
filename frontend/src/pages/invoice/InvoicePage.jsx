// src/pages/invoice/InvoicePage.jsx
// Module 07 – Invoice
// Trang danh sách hóa đơn: Table + Bộ lọc + Xem chi tiết + Tải PDF
// TUÂN THỦ TẤT CẢ RULES Project – Quán ăn

import { useEffect, useState } from "react";
import {
  Table,
  Button,
  DatePicker,
  Input,
  Row,
  Col,
  message,
  Space,
  Modal,
} from "antd";
import dayjs from "dayjs";

import {
  getInvoices,
  filterInvoices,
  getInvoiceDetail,
  downloadInvoicePdf,
} from "../../api/invoiceApi";

import InvoiceDetailModal from "../../components/invoice/InvoiceDetailModal";
import PaymentModal from "../../components/payment/PaymentModal";

const { RangePicker } = DatePicker;

/**
 * Format ngày theo Rule 15: dd/MM/yyyy HH:mm
 */
const formatDate = (value) => {
  if (!value) return "";
  return dayjs(value).format("DD/MM/YYYY HH:mm");
};

export default function InvoicePage() {
  /**
   * ===========================
   * STATE
   * ===========================
   */
  const [invoices, setInvoices] = useState([]);
  const [loading, setLoading] = useState(false);

  // Bộ lọc
  const [dateRange, setDateRange] = useState([]);
  const [keyword, setKeyword] = useState("");

  // State xem chi tiết
  const [detailOpen, setDetailOpen] = useState(false);
  const [invoiceDetail, setInvoiceDetail] = useState(null);

  // ===========================
  // STATE PAYMENT MODAL
  // ===========================
  const [paymentOpen, setPaymentOpen] = useState(false);
  const [selectedInvoice, setSelectedInvoice] = useState(null);

  /**
   * ===========================
   * LOAD DANH SÁCH HÓA ĐƠN
   * ===========================
   */
  const loadInvoices = async () => {
    try {
      setLoading(true);
      const data = await getInvoices(); // trả về data thuần
      setInvoices(data);
    } catch (err) {
      console.error(err);
      message.error("Lỗi tải danh sách hóa đơn!");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadInvoices();
  }, []);

  /**
   * ===========================
   * HÀM LỌC HÓA ĐƠN
   * ===========================
   */
  const handleFilter = async () => {
    try {
      setLoading(true);

      const body = {
        fromDate: dateRange[0] ? dateRange[0].format("YYYY-MM-DD") : null,
        toDate: dateRange[1] ? dateRange[1].format("YYYY-MM-DD") : null,
        keyword: keyword || null,
      };

      const data = await filterInvoices(body);
      setInvoices(data);
    } catch (err) {
      console.error(err);
      message.error("Lỗi lọc hóa đơn!");
    } finally {
      setLoading(false);
    }
  };

  /**
   * ===========================
   * XEM CHI TIẾT HÓA ĐƠN
   * ===========================
   */
  const openDetail = async (id) => {
    try {
      const data = await getInvoiceDetail(id);
      setInvoiceDetail(data);
      setDetailOpen(true);
    } catch (err) {
      console.error(err);
      message.error("Lỗi tải chi tiết hóa đơn!");
    }
  };

  /**
   * Mở modal thanh toán hóa đơn
   */
  const openPaymentModal = async (id) => {
    try {
      // Lấy chi tiết hóa đơn để truyền xuống modal
      const data = await getInvoiceDetail(id);
      setSelectedInvoice(data);
      setPaymentOpen(true);
    } catch (err) {
      console.error(err);
      message.error("Không tải được thông tin hóa đơn để thanh toán!");
    }
  };

  /**
   * ===========================
   * TẢI PDF HÓA ĐƠN
   * ===========================
   */
  const handleDownloadPdf = async (id) => {
    try {
      const blob = await downloadInvoicePdf(id);

      // Tạo link download thủ công
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = `invoice_${id}.pdf`;
      a.click();

      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error(err);
      message.error("Không tải được PDF!");
    }
  };

  /**
   * ===========================
   * TABLE COLUMNS
   * ===========================
   */
  const columns = [
    {
      title: "Mã hóa đơn",
      dataIndex: "code",
      key: "code",
    },
    {
      title: "Mã đơn hàng",
      dataIndex: "orderId",
      key: "orderId",
    },
    {
      title: "Tổng tiền",
      dataIndex: "totalAmount",
      key: "totalAmount",
      render: (v) => v?.toLocaleString() + " đ",
    },
    {
      title: "Ngày tạo",
      dataIndex: "createdAt",
      key: "createdAt",
      render: (v) => formatDate(v),
    },
    {
      title: "Người tạo",
      dataIndex: "createdBy",
      key: "createdBy",
    },
    {
      title: "Trạng thái",
      dataIndex: "status",
      render: (v) =>
        v === "PAID" ? (
          <span style={{ color: "green", fontWeight: "bold" }}>ĐÃ THANH TOÁN</span>
        ) : (
          <span style={{ color: "red", fontWeight: "bold" }}>CHƯA THANH TOÁN</span>
        ),
    },
    {
      title: "Hành động",
      key: "actions",
      render: (_, record) => (
        <Space>
          {/* Nút xem chi tiết */}
          <Button type="primary" onClick={() => openDetail(record.id)}>
            Xem
          </Button>

          {/* HIỆN NÚT THANH TOÁN NẾU CHƯA PAID */}
          {record.status !== "PAID" && (
            <Button
              style={{ background: "#52c41a", color: "white", borderColor: "#52c41a" }}
              onClick={() => openPaymentModal(record.id)}
            >
              Thanh toán
            </Button>
          )}

          {/* Nút tải PDF */}
          <Button danger onClick={() => handleDownloadPdf(record.id)}>
            PDF
          </Button>
        </Space>
      ),
    },
  ];

  /**
   * Xử lý thanh toán hóa đơn
   * - Hiện modal confirm
   * - Gọi API payInvoice()
   * - Reload lại danh sách sau khi thanh toán
   */
  /*
  const handlePay = (id) => {
    Modal.confirm({
      title: "Xác nhận thanh toán",
      content: "Bạn có chắc muốn thanh toán hóa đơn này?",
      okText: "Thanh toán",
      okType: "primary",
      cancelText: "Hủy",

      // Khi bấm "Thanh toán"
      onOk: async () => {
        try {
          await payInvoice(id); // gọi API FE
          message.success("Thanh toán thành công!");
          loadInvoices(); // reload lại bảng
        } catch (err) {
          message.error("Thanh toán thất bại!");
        }
      },
    });
  }; */

  return (
    <div>
      <h2 style={{ marginBottom: 20 }}>Danh sách hóa đơn</h2>

      {/* Bộ lọc */}
      <Row gutter={16} style={{ marginBottom: 20 }}>
        <Col span={8}>
          <label>Khoảng ngày:</label>
          <RangePicker
            style={{ width: "100%" }}
            format="DD/MM/YYYY"
            value={dateRange}
            onChange={(value) => setDateRange(value)}
          />
        </Col>

        <Col span={6}>
          <label>Tìm kiếm:</label>
          <Input
            placeholder="Mã hóa đơn / Người tạo"
            value={keyword}
            onChange={(e) => setKeyword(e.target.value)}
          />
        </Col>

        <Col span={4} style={{ display: "flex", alignItems: "flex-end" }}>
          <Button type="primary" onClick={handleFilter}>
            Lọc
          </Button>
        </Col>

        <Col span={4} style={{ display: "flex", alignItems: "flex-end" }}>
          <Button onClick={loadInvoices}>Xóa bộ lọc</Button>
        </Col>
      </Row>

      {/* Bảng danh sách */}
      <Table
        rowKey="id"
        columns={columns}
        dataSource={invoices}
        loading={loading}
        pagination={{ pageSize: 10 }}
      />

      {/* Modal chi tiết */}
      {detailOpen && (
        <InvoiceDetailModal
          open={detailOpen}
          onClose={() => setDetailOpen(false)}
          data={invoiceDetail}
        />
      )}

      {paymentOpen && (
        <PaymentModal
          open={paymentOpen}
          onClose={() => setPaymentOpen(false)}
          invoice={selectedInvoice}
          onSuccess={loadInvoices} // reload lại danh sách sau khi thanh toán
        />
      )}
    </div>
  );
}
