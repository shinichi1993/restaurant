// src/components/ingredient/IngredientForm.jsx

import { Form, Input, InputNumber } from "antd";

/**
 * Form nhập liệu cho nguyên liệu
 * Dùng chung cho Thêm / Sửa
 */
export default function IngredientForm({ form }) {
  return (
    <Form
      form={form}
      layout="vertical"
    >
      {/* Tên nguyên liệu */}
      <Form.Item
        label="Tên nguyên liệu"
        name="name"
        rules={[
          { required: true, message: "Vui lòng nhập tên nguyên liệu" },
        ]}
      >
        <Input placeholder="VD: Thịt bò" />
      </Form.Item>

      {/* Đơn vị tính */}
      <Form.Item
        label="Đơn vị tính"
        name="unit"
        rules={[
          { required: true, message: "Vui lòng nhập đơn vị tính" },
        ]}
      >
        <Input placeholder="VD: gram, ml, cái..." />
      </Form.Item>

      {/* Tồn kho hiện tại */}
      <Form.Item
        label="Tồn kho hiện tại"
        name="stockQuantity"
        rules={[
          { required: true, message: "Vui lòng nhập tồn kho hiện tại" },
        ]}
      >
        <InputNumber
          style={{ width: "100%" }}
          min={0}
          placeholder="VD: 5000"
        />
      </Form.Item>

      {/* Ngưỡng tồn kho tối thiểu */}
      <Form.Item
        label="Ngưỡng cảnh báo tồn kho thấp"
        name="minStock"
        rules={[
          { required: true, message: "Vui lòng nhập ngưỡng cảnh báo" },
        ]}
      >
        <InputNumber
          style={{ width: "100%" }}
          min={0}
          placeholder="VD: 500"
        />
      </Form.Item>
    </Form>
  );
}
