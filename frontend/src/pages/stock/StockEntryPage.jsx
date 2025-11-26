import { useEffect, useState } from "react";
import {
  Button,
  Table,
  Modal,
  Form,
  InputNumber,
  Select,
  Input,
  message,
} from "antd";
import dayjs from "dayjs";

import { getIngredients } from "../../api/ingredientApi";
import { getStockEntries, createStockEntry } from "../../api/stockEntryApi";
import { PlusOutlined } from "@ant-design/icons";

/**
 * Trang quản lý phiếu nhập kho
 * - Xem danh sách phiếu nhập
 * - Thêm phiếu nhập (KHÔNG có sửa/xóa theo Rule A)
 */
export default function StockEntryPage() {
  const [entries, setEntries] = useState([]);
  const [ingredients, setIngredients] = useState([]);

  const [openModal, setOpenModal] = useState(false);
  const [form] = Form.useForm();

  /**
   * Load nguyên liệu + phiếu nhập khi page mở
   */
  useEffect(() => {
    loadIngredients();
    loadEntries();
  }, []);

  const loadIngredients = async () => {
    try {
      const res = await getIngredients();

      // ✅ SỬA 1: getIngredients() đã return data trực tiếp,
      // KHÔNG còn dùng res.data nữa → dùng res thay vì res.data
      setIngredients(res || []);
    } catch (e) {
      console.error("Lỗi load nguyên liệu:", e);
      setIngredients([]); // fallback
    }
  };

  const loadEntries = async () => {
    try {
      const res = await getStockEntries();
      setEntries(res.data || []);
    } catch (e) {
      console.error("Lỗi load phiếu nhập:", e);
      setEntries([]);
    }
  };

  /**
   * Xử lý tạo phiếu nhập
   */
  const handleCreate = async () => {
    try {
      const values = await form.validateFields();

      await createStockEntry(values);

      message.success("Tạo phiếu nhập kho thành công!");

      form.resetFields();
      setOpenModal(false);
      loadEntries();
    } catch (err) {
      console.error(err);
      message.error("Không thể tạo phiếu nhập!");
    }
  };

  /**
   * Cấu hình các cột của bảng
   */
  const columns = [
    {
      title: "Mã phiếu",
      dataIndex: "id",
      key: "id",
      width: 80,
    },
    {
      title: "Nguyên liệu",
      dataIndex: "ingredientName",
      key: "ingredientName",
    },
    {
      title: "Số lượng nhập",
      dataIndex: "quantity",
      key: "quantity",
      width: 120,
    },
    {
      title: "Ghi chú",
      dataIndex: "note",
      key: "note",
    },
    {
      title: "Ngày tạo",
      dataIndex: "createdAt",
      key: "createdAt",
      width: 180,
      render: (value) => dayjs(value).format("DD/MM/YYYY HH:mm"),
    },
    {
      title: "Người tạo",
      dataIndex: "createdBy",
      key: "createdBy",
      width: 100,
    },
  ];

  return (
    <div>
      {/* HEADER */}
      <div
        style={{
          marginBottom: 20,
          display: "flex",
          justifyContent: "space-between",
        }}
      >
        <h2>Quản lý nhập kho</h2>

        {/* Nút Thêm – theo Rule 16 */}
        <Button
          type="primary"
          icon={<PlusOutlined />}
          onClick={() => setOpenModal(true)}
        >
          Thêm
        </Button>
      </div>

      {/* TABLE */}
      <Table
        dataSource={entries}
        columns={columns}
        rowKey="id"
        bordered
        pagination={{ pageSize: 10 }}
      />

      {/* MODAL THÊM */}
      <Modal
        title="Tạo phiếu nhập kho"
        open={openModal}
        onCancel={() => setOpenModal(false)}
        onOk={handleCreate}
        okText="Lưu"
        cancelText="Hủy"
      >
        <Form form={form} layout="vertical">

          {/* Chọn nguyên liệu */}
          <Form.Item
            label="Nguyên liệu"
            name="ingredientId"
            rules={[{ required: true, message: "Vui lòng chọn nguyên liệu" }]}
          >
            {/* 
              ✅ SỬA 2: Ant Design v5 KHÔNG hỗ trợ <Select.Option>.
              PHẢI dùng props options=[...] thì dropdown mới hiển thị.
            */}
            <Select
              placeholder="Chọn nguyên liệu"
              options={ingredients.map((item) => ({
                label: item.name,
                value: item.id,
              }))}
            />
          </Form.Item>

          {/* Số lượng nhập */}
          {/* Số lượng nhập (có thể âm để điều chỉnh) */}
          <Form.Item
            label="Số lượng nhập"
            name="quantity"
            rules={[{ required: true, message: "Vui lòng nhập số lượng" }]}
          >
            {/* ❗ Bỏ min=1 để cho phép nhập số âm.
                Nếu muốn giới hạn, có thể dùng min={-999999} */}
            <InputNumber
              placeholder="Nhập số lượng (có thể âm để điều chỉnh)"
              style={{ width: "100%" }}
            />
          </Form.Item>

          {/* Ghi chú */}
          <Form.Item label="Ghi chú" name="note">
            <Input.TextArea placeholder="Nhập ghi chú (nếu có)" rows={3} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}
