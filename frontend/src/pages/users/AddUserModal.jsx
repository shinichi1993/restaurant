import { Modal, Form, Input, message } from "antd";
import axiosClient from "../../api/axiosClient";

/**
 * Modal thêm user mới
 */
export default function AddUserModal({ open, onClose, onSuccess }) {
  const [form] = Form.useForm();

  const onSubmit = async () => {
    try {
      const values = await form.validateFields();

      // Gọi API tạo user
      await axiosClient.post("/users", {
        fullName: values.fullName,
        email: values.email,
        phone: values.phone,
        password: values.password,
      });

      message.success("Tạo user thành công!");
      form.resetFields();
      onSuccess();
      onClose();
    } catch (e) {
      message.error("Không thể tạo user");
    }
  };

  return (
    <Modal
      open={open}
      title="Thêm User"
      okText="Lưu"
      cancelText="Hủy"
      onOk={onSubmit}
      onCancel={onClose}
    >
      <Form layout="vertical" form={form}>
        <Form.Item
          label="Full Name"
          name="fullName"
          rules={[{ required: true, message: "Nhập tên user" }]}
        >
          <Input />
        </Form.Item>

        <Form.Item
          label="Email"
          name="email"
          rules={[{ required: true, message: "Nhập email" }]}
        >
          <Input />
        </Form.Item>

        <Form.Item label="Phone" name="phone">
          <Input />
        </Form.Item>

        <Form.Item
          label="Password"
          name="password"
          rules={[{ required: true, message: "Nhập password" }]}
        >
          <Input.Password />
        </Form.Item>
      </Form>
    </Modal>
  );
}
