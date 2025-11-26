// src/components/payment/PaymentModal.jsx
// Modal thanh toán hóa đơn – CHUẨN BACKEND MODULE 08
// Không có note, không có paidAt trong request

import { Modal, Form, InputNumber, Select, message } from "antd";
import { createPayment } from "../../api/paymentApi";

export default function PaymentModal({ open, onClose, invoice, onSuccess }) {
  const [form] = Form.useForm();

  /**
   * Submit thanh toán
   */
  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();

      // Body gửi theo PaymentCreateRequest (Module 08)
      const body = {
        amount: values.amount,
        method: values.method,
        status: "SUCCESS",            // Backend yêu cầu status, FE tự gán SUCCESS
        transactionCode: null         // Tạm thời null (sau này online mới dùng)
      };

      await createPayment(invoice.id, body);

      message.success("Thanh toán thành công!");
      onSuccess(); // reload danh sách invoice
      onClose();
      form.resetFields();

    } catch (err) {
      console.error(err);
      message.error("Thanh toán thất bại!");
    }
  };

  return (
    <Modal
      open={open}
      title={`Thanh toán hóa đơn: ${invoice?.code || ""}`}
      onCancel={onClose}
      onOk={handleSubmit}
      okText="Xác nhận thanh toán"
      cancelText="Hủy"
    >
      <Form form={form} layout="vertical">

        {/* Phương thức thanh toán */}
        <Form.Item
          label="Phương thức thanh toán"
          name="method"
          rules={[{ required: true, message: "Vui lòng chọn phương thức!" }]}
        >
          <Select placeholder="Chọn phương thức">
            <Select.Option value="CASH">Tiền mặt</Select.Option>
            <Select.Option value="MOMO">Momo</Select.Option>
            <Select.Option value="BANK">Chuyển khoản ngân hàng</Select.Option>
            <Select.Option value="CARD">Thẻ Visa/MasterCard</Select.Option>
          </Select>
        </Form.Item>

        {/* Số tiền thanh toán */}
        <Form.Item
          label="Số tiền thanh toán"
          name="amount"
          rules={[{ required: true, message: "Vui lòng nhập số tiền!" }]}
        >
          <InputNumber
            style={{ width: "100%" }}
            min={0}
            placeholder="Nhập số tiền"
            formatter={(value) =>
              `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ",")
            }
            parser={(value) => value.replace(/,/g, "")}
          />
        </Form.Item>
      </Form>
    </Modal>
  );
}
