// FIXED VERSION — chống undefined ingredients + đảm bảo Form nhận prop form

import { Form, InputNumber, Select } from "antd";

export default function RecipeForm({ form, ingredients }) {
  return (
    <Form form={form} layout="vertical">
      {/* Chọn nguyên liệu */}
      <Form.Item
        label="Nguyên liệu"
        name="ingredientId"
        rules={[{ required: true, message: "Vui lòng chọn nguyên liệu" }]}
      >
        <Select
          placeholder="Chọn nguyên liệu"
          // FIX: luôn map trên [] nếu ingredients = undefined
          options={(ingredients || []).map((i) => ({
            value: i.id,
            label: i.name,
          }))}
        />
      </Form.Item>

      {/* Số lượng cần */}
      <Form.Item
        label="Số lượng cần (gram/ml)"
        name="quantityNeeded"
        rules={[{ required: true, message: "Vui lòng nhập số lượng" }]}
      >
        <InputNumber
          min={1}
          style={{ width: "100%" }}
          placeholder="Nhập số gram/ml"
        />
      </Form.Item>
    </Form>
  );
}
