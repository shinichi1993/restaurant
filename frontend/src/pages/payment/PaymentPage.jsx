// src/pages/payment/PaymentPage.jsx
// Trang quản lý danh sách thanh toán (Payment)

import { useEffect, useState } from "react";
import {
  Table,
  Button,
  Form,
  Select,
  DatePicker,
  Row,
  Col,
  Space,
  Tag,
  message,
} from "antd";
import dayjs from "dayjs";

import {
  fetchPayments,
  getPaymentDetail,
} from "../../api/paymentApi";
import PaymentDetailModal from "../../components/payment/PaymentDetailModal";

// Hàm format datetime theo rule: dd/MM/yyyy HH:mm
const formatDateTime = (value) => {
  if (!value) return "";
  return dayjs(value).format("DD/MM/YYYY HH:mm");
};

const { RangePicker } = DatePicker;

// Giá trị options cho phương thức thanh toán
const METHOD_OPTIONS = [
  { label: "Tất cả", value: "" },
  { label: "Tiền mặt", value: "CASH" },
  { label: "Momo", value: "MOMO" },
  { label: "Chuyển khoản", value: "BANK" },
];

// Giá trị options cho trạng thái thanh toán
const STATUS_OPTIONS = [
  { label: "Tất cả", value: "" },
  { label: "Thành công", value: "SUCCESS" },
  { label: "Thất bại", value: "FAILED" },
  { label: "Đang xử lý", value: "PENDING" },
];

export default function PaymentPage() {
  // Danh sách payment hiển thị trong bảng
  const [payments, setPayments] = useState([]);

  // Trạng thái loading khi gọi API
  const [loading, setLoading] = useState(false);

  // Thông tin phân trang: current (1-based), pageSize, total
  const [pagination, setPagination] = useState({
    current: 1,
    pageSize: 10,
    total: 0,
  });

  // State filter trên UI
  const [filters, setFilters] = useState({
    method: "",
    status: "",
    fromDate: null, // dayjs hoặc null
    toDate: null,   // dayjs hoặc null
  });

  // State cho modal chi tiết
  const [detailVisible, setDetailVisible] = useState(false);
  const [selectedPayment, setSelectedPayment] = useState(null);

  // Form instance cho filter
  const [form] = Form.useForm();

  /**
   * Hàm load dữ liệu payment từ backend.
   * - Dựa trên filters + pagination.
   */
  const loadPayments = async (override = {}) => {
    try {
      setLoading(true);

      // Lấy current/pageSize mới (cho phép override khi đổi trang)
      const current =
        override.current !== undefined
            ? override.current
            : pagination.current;
      const pageSize = override.pageSize ?? pagination.pageSize;

      // Chuyển ngày sang string "YYYY-MM-DD" như backend yêu cầu
      const fromDateStr = filters.fromDate
        ? filters.fromDate.format("YYYY-MM-DD")
        : undefined;
      const toDateStr = filters.toDate
        ? filters.toDate.format("YYYY-MM-DD")
        : undefined;

      const params = {
        fromDate: fromDateStr,
        toDate: toDateStr,
        method: filters.method || undefined,
        status: filters.status || undefined,
        page: current - 1,   // FE 1-based, BE 0-based
        size: pageSize,
      };

      const data = await fetchPayments(params);

      // Backend trả về Page<PaymentResponse>:
      // { content, totalElements, number, size, ... }
      setPayments(data.content || []);
      setPagination({
        current: (data.number ?? 0) + 1,
        pageSize: data.size ?? pageSize,
        total: data.totalElements ?? 0,
      });
    } catch (error) {
      console.error("Lỗi tải danh sách payment:", error);
      message.error("Không tải được danh sách thanh toán");
    } finally {
      setLoading(false);
    }
  };

  // Load lần đầu khi mount
  useEffect(() => {
    loadPayments();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Mỗi khi filters thay đổi → tự động reload data từ page 1
    useEffect(() => {
        // Reset pagination về trang 1 trước
        setPagination((prev) => ({
            ...prev,
            current: 1,
        }));
        loadPayments({ current: 1 });
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [filters]);

  /**
   * Xử lý khi bấm nút "Lọc".
   * Lưu lại filters và gọi loadPayments.
   */
  const handleFilter = () => {
  const values = form.getFieldsValue();

    setFilters({
        method: values.method || "",
        status: values.status || "",
        fromDate: values.dateRange ? values.dateRange[0] : null,
        toDate: values.dateRange ? values.dateRange[1] : null,
    });

    // KHÔNG gọi loadPayments() ở đây nữa
    };

  /**
   * Xử lý khi bấm "Xóa lọc".
   * Reset form + filters + load lại trang 1.
   */
  const handleResetFilter = () => {
    form.resetFields();
    setFilters({
      method: "",
      status: "",
      fromDate: null,
      toDate: null,
    });
  };

  /**
   * Xử lý khi đổi trang hoặc pageSize trên bảng.
   */
  const handleTableChange = (newPagination) => {
    loadPayments({
      current: newPagination.current,
      pageSize: newPagination.pageSize,
    });
  };

  /**
   * Mở modal chi tiết cho 1 payment.
   * Gọi API lấy chi tiết để đảm bảo dữ liệu mới nhất.
   */
  const openDetail = async (paymentId) => {
    try {
      setLoading(true);
      const data = await getPaymentDetail(paymentId);
      setSelectedPayment(data);
      setDetailVisible(true);
    } catch (error) {
      console.error("Lỗi lấy chi tiết payment:", error);
      message.error("Không lấy được chi tiết thanh toán");
    } finally {
      setLoading(false);
    }
  };

  /**
   * Đóng modal chi tiết.
   */
  const closeDetail = () => {
    setDetailVisible(false);
    setSelectedPayment(null);
  };

  // Định nghĩa các cột của bảng payment
  const columns = [
    {
      title: "Mã hóa đơn",
      dataIndex: "invoiceCode",
      key: "invoiceCode",
    },
    {
      title: "Mã đơn hàng",
      dataIndex: "orderId",
      key: "orderId",
    },
    {
      title: "Số tiền thanh toán",
      dataIndex: "amount",
      key: "amount",
      render: (value) =>
        value != null ? value.toLocaleString("vi-VN") + " ₫" : "",
    },
    {
      title: "Phương thức",
      dataIndex: "method",
      key: "method",
      render: (value) => {
        if (value === "CASH") return "Tiền mặt";
        if (value === "MOMO") return "Momo";
        if (value === "BANK") return "Chuyển khoản";
        return value || "";
      },
    },
    {
      title: "Trạng thái",
      dataIndex: "status",
      key: "status",
      render: (value) => {
        let color = "default";
        let text = value;

        if (value === "SUCCESS") {
          color = "green";
          text = "Thành công";
        } else if (value === "FAILED") {
          color = "red";
          text = "Thất bại";
        } else if (value === "PENDING") {
          color = "orange";
          text = "Đang xử lý";
        }

        return <Tag color={color}>{text}</Tag>;
      },
    },
    {
      title: "Ngày tạo",
      dataIndex: "createdAt",
      key: "createdAt",
      render: (value) => formatDateTime(value),
    },
    {
      title: "Ngày thanh toán",
      dataIndex: "paidAt",
      key: "paidAt",
      render: (value) => formatDateTime(value),
    },
    {
      title: "Hành động",
      key: "action",
      render: (_, record) => (
        <Button type="link" onClick={() => openDetail(record.id)}>
          Xem chi tiết
        </Button>
      ),
    },
  ];

  return (
    <div>
      <h2>Quản lý thanh toán</h2>

      {/* Khu vực filter */}
      <Form
        form={form}
        layout="vertical"
        onFinish={handleFilter}
        style={{ marginBottom: 16 }}
      >
        <Row gutter={16}>
          <Col xs={24} sm={12} md={6}>
            <Form.Item label="Phương thức thanh toán" name="method">
              <Select
                placeholder="Chọn phương thức"
                options={METHOD_OPTIONS}
                allowClear
              />
            </Form.Item>
          </Col>

          <Col xs={24} sm={12} md={6}>
            <Form.Item label="Trạng thái" name="status">
              <Select
                placeholder="Chọn trạng thái"
                options={STATUS_OPTIONS}
                allowClear
              />
            </Form.Item>
          </Col>

          <Col xs={24} sm={24} md={8}>
            <Form.Item label="Khoảng thời gian" name="dateRange">
              <RangePicker
                style={{ width: "100%" }}
                format="DD/MM/YYYY"
                placeholder={["Từ ngày", "Đến ngày"]}
              />
            </Form.Item>
          </Col>

          <Col
            xs={24}
            sm={24}
            md={4}
            style={{ display: "flex", alignItems: "flex-end" }}
          >
            <Space>
              <Button type="primary" htmlType="submit">
                Lọc
              </Button>
              <Button onClick={handleResetFilter}>Xóa lọc</Button>
            </Space>
          </Col>
        </Row>
      </Form>

      {/* Bảng danh sách payment */}
      <Table
        rowKey="id"
        loading={loading}
        columns={columns}
        dataSource={payments}
        pagination={pagination}
        onChange={handleTableChange}
      />

      {/* Modal chi tiết payment */}
      <PaymentDetailModal
        visible={detailVisible}
        payment={selectedPayment}
        onClose={closeDetail}
      />
    </div>
  );
}
