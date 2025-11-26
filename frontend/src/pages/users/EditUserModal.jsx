import { Modal, Form, Input, message } from "antd";
import axiosClient from "../../api/axiosClient";
import { useEffect } from "react";

/**
 * EditUserModal – modal chỉnh sửa thông tin user
 */
export default function EditUserModal({ open, onClose, user, onSuccess }) {
  const [form] = Form.useForm();

  // Khi modal mở → load dữ liệu user vào form
  useEffect(() => {
    if (user) {
      form.setFieldsValue({
        fullName: user.fullName,
        email: user.email,
        phone: user.phone,
      });
    }
  }, [user]);

  const onSubmit = async () => {
    try {
      const values = await form.validateFields();

      // Gọi API cập nhật user
      await axiosClient.put(`/users/${user.id}`, {
        fullName: values.fullName,
        email: values.email, // email giữ nguyên
        phone: values.phone,
      });

      message.success("Cập nhật user thành công!");
      onSuccess();
      onClose();
    } catch (err) {
      message.error("Không thể cập nhật user");
    }
  };

  return (
    <Modal
      open={open}
      title="Chỉnh sửa User"
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

        <Form.Item label="Email" name="email">
          <Input disabled /> {/* Không cho sửa email */}
        </Form.Item>

        <Form.Item label="Phone" name="phone">
          <Input />
        </Form.Item>

      </Form>
    </Modal>
  );
}
