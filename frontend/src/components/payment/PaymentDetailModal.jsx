// src/components/payment/PaymentDetailModal.jsx
// Modal hiển thị chi tiết 1 payment

import { Modal, Descriptions } from "antd";
import dayjs from "dayjs";

// Hàm format datetime theo rule: dd/MM/yyyy HH:mm
const formatDateTime = (value) => {
  if (!value) return "";
  return dayjs(value).format("DD/MM/YYYY HH:mm");
};

export default function PaymentDetailModal({ visible, payment, onClose }) {
  // Nếu chưa có dữ liệu payment thì không hiển thị nội dung
  if (!payment) {
    return (
      <Modal
        open={visible}
        onCancel={onClose}
        footer={null}
        title="Chi tiết thanh toán"
      >
        Không có dữ liệu thanh toán.
      </Modal>
    );
  }

  return (
    <Modal
      open={visible}
      onCancel={onClose}
      footer={null}
      title="Chi tiết thanh toán"
    >
      <Descriptions column={1} bordered size="small">
        <Descriptions.Item label="Mã hóa đơn">
          {payment.invoiceCode}
        </Descriptions.Item>
        <Descriptions.Item label="Mã đơn hàng">
          {payment.orderId}
        </Descriptions.Item>
        <Descriptions.Item label="Số tiền thanh toán">
          {payment.amount != null
            ? payment.amount.toLocaleString("vi-VN") + " ₫"
            : ""}
        </Descriptions.Item>
        <Descriptions.Item label="Phương thức">
          {payment.method === "CASH"
            ? "Tiền mặt"
            : payment.method === "MOMO"
            ? "Momo"
            : payment.method === "BANK"
            ? "Chuyển khoản"
            : payment.method}
        </Descriptions.Item>
        <Descriptions.Item label="Trạng thái">
          {payment.status === "SUCCESS"
            ? "Thành công"
            : payment.status === "FAILED"
            ? "Thất bại"
            : payment.status === "PENDING"
            ? "Đang xử lý"
            : payment.status}
        </Descriptions.Item>
        <Descriptions.Item label="Mã giao dịch">
          {payment.transactionCode || ""}
        </Descriptions.Item>
        <Descriptions.Item label="Ngày tạo">
          {formatDateTime(payment.createdAt)}
        </Descriptions.Item>
        <Descriptions.Item label="Ngày thanh toán">
          {formatDateTime(payment.paidAt)}
        </Descriptions.Item>
      </Descriptions>
    </Modal>
  );
}
