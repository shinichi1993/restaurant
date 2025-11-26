import { Form, Input, InputNumber, Select } from "antd";

export default function DishForm({ form, categories, onFinish }) {
  return (
    <Form layout="vertical" form={form} onFinish={onFinish}>
      <Form.Item
        label="Tên món ăn"
        name="name"
        rules={[{ required: true, message: "Tên món không được để trống" }]}
      >
        <Input placeholder="Nhập tên món ăn" />
      </Form.Item>

      <Form.Item label="Mô tả" name="description">
        <Input.TextArea rows={3} placeholder="Nhập mô tả món (không bắt buộc)" />
      </Form.Item>

      <Form.Item
        label="Giá bán (VNĐ)"
        name="price"
        rules={[{ required: true, message: "Giá món không được để trống" }]}
      >
        <InputNumber style={{ width: "100%" }} min={0} placeholder="Nhập giá món" />
      </Form.Item>

      <Form.Item label="Link ảnh" name="imageUrl">
        <Input placeholder="Nhập URL ảnh món (không bắt buộc)" />
      </Form.Item>

      <Form.Item
        label="Danh mục"
        name="categoryId"
        rules={[{ required: true, message: "Vui lòng chọn danh mục" }]}
      >
        <Select placeholder="Chọn danh mục">
          {categories.map((c) => (
            <Select.Option key={c.id} value={c.id}>
              {c.name}
            </Select.Option>
          ))}
        </Select>
      </Form.Item>
    </Form>
  );
}
