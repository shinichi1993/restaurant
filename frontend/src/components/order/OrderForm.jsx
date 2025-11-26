import { useEffect } from "react";
import { Form, Input, Select, InputNumber, Button, Row, Col } from "antd";

/**
 * Form nhập đơn hàng
 * - customerName
 * - customerPhone
 * - tableNumber
 * - note
 * - items: danh sách món ăn
 */
export default function OrderForm({ dishes, initialValues, onSubmit }) {
  const [form] = Form.useForm();

  // Đổ dữ liệu khi sửa
  useEffect(() => {
    if (initialValues) {
      form.setFieldsValue(initialValues);
    }
  }, [initialValues]);

  // Thêm item trống khi tạo mới
  const addItem = () => {
    const items = form.getFieldValue("items") || [];
    form.setFieldsValue({
      items: [
        ...items,
        {
          dishId: null,
          quantity: 1,
        },
      ],
    });
  };

  return (
    <Form
      form={form}
      layout="vertical"
      onFinish={onSubmit}
      initialValues={{ items: [{ dishId: null, quantity: 1 }] }}
    >
      <Row gutter={16}>
        <Col span={12}>
          <Form.Item label="Tên khách" name="customerName">
            <Input placeholder="Nhập tên khách..." />
          </Form.Item>
        </Col>

        <Col span={12}>
          <Form.Item label="Số điện thoại" name="customerPhone">
            <Input placeholder="Nhập số điện thoại..." />
          </Form.Item>
        </Col>

        <Col span={12}>
          <Form.Item label="Số bàn" name="tableNumber">
            <Input placeholder="Ví dụ: A1, B2..." />
          </Form.Item>
        </Col>

        <Col span={12}>
          <Form.Item label="Ghi chú" name="note">
            <Input placeholder="Ghi chú cho bếp..." />
          </Form.Item>
        </Col>
      </Row>

      {/* Danh sách món */}
      <h4>Món ăn</h4>

      <Form.List name="items">
        {(fields) => (
          <>
            {fields.map((field) => (
              <Row gutter={16} key={field.key} style={{ marginBottom: 10 }}>
                <Col span={12}>
                  <Form.Item
                    {...field}
                    name={[field.name, "dishId"]}
                    label="Chọn món"
                    rules={[{ required: true, message: "Hãy chọn món" }]}
                  >
                    <Select placeholder="Chọn món...">
                      {dishes.map((d) => (
                        <Select.Option value={d.id} key={d.id}>
                          {d.name} – {d.price}đ
                        </Select.Option>
                      ))}
                    </Select>
                  </Form.Item>
                </Col>

                <Col span={12}>
                  <Form.Item
                    {...field}
                    name={[field.name, "quantity"]}
                    label="Số lượng"
                    rules={[{ required: true, message: "Nhập số lượng" }]}
                  >
                    <InputNumber min={1} style={{ width: "100%" }} />
                  </Form.Item>
                </Col>
              </Row>
            ))}

            <Button type="dashed" onClick={addItem} block>
              + Thêm món
            </Button>
          </>
        )}
      </Form.List>

      <div style={{ marginTop: 20, textAlign: "right" }}>
        <Button type="primary" htmlType="submit">
          Lưu đơn hàng
        </Button>
      </div>
    </Form>
  );
}
