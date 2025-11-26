// src/components/category/CategoryForm.jsx

import { Form, Input } from "antd";

/**
 * CategoryForm
 * - Form dùng chung cho Create + Update
 * - Dùng Form antd (layout vertical)
 * - Không chứa logic, chỉ hiển thị input
 */
export default function CategoryForm({ form, onFinish }) {
  return (
    <Form
      form={form}
      layout="vertical"
      onFinish={onFinish}   // ⭐ Gọi hàm xử lý create/update từ page
    >
      {/* Tên danh mục */}
      <Form.Item
        label="Tên danh mục"
        name="name"
        rules={[{ required: true, message: "Tên danh mục không được để trống" }]}
      >
        <Input placeholder="Nhập tên danh mục" />
      </Form.Item>

      {/* Mô tả */}
      <Form.Item label="Mô tả" name="description">
        <Input.TextArea rows={3} placeholder="Nhập mô tả (không bắt buộc)" />
      </Form.Item>
    </Form>
  );
}
