// src/pages/ingredient/IngredientPage.jsx

import { useEffect, useState } from "react";
import {
  Button,
  Modal,
  Table,
  message,
  Form,
  Popconfirm,
  Tag,
  Space,
} from "antd";

import {
  getIngredients,
  createIngredient,
  updateIngredient,
  deleteIngredient,
} from "../../api/ingredientApi";

import IngredientForm from "../../components/ingredient/IngredientForm";

/**
 * Trang quản lý nguyên liệu
 * - Danh sách nguyên liệu (Table)
 * - Thêm / Sửa / Xóa nguyên liệu
 * - Hiển thị cảnh báo khi tồn kho < minStock
 */
export default function IngredientPage() {
  // Danh sách nguyên liệu
  const [ingredients, setIngredients] = useState([]);

  // Loading table
  const [loading, setLoading] = useState(false);

  // Modal
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalTitle, setModalTitle] = useState("Thêm nguyên liệu");

  // Mode: "create" | "edit"
  const [mode, setMode] = useState("create");
  const [currentId, setCurrentId] = useState(null);

  // Form antd
  const [form] = Form.useForm();

  /**
   * Load danh sách nguyên liệu từ backend
   */
  const loadData = async () => {
    try {
      setLoading(true);
      const data = await getIngredients();
      setIngredients(data);
    } catch (error) {
      console.error(error);
      message.error("Không thể tải danh sách nguyên liệu");
    } finally {
      setLoading(false);
    }
  };

  // Gọi API lần đầu
  useEffect(() => {
    loadData();
  }, []);

  /**
   * Mở modal ở chế độ thêm mới
   */
  const openCreateModal = () => {
    setMode("create");
    setModalTitle("Thêm nguyên liệu");
    setCurrentId(null);
    form.resetFields();
    setIsModalOpen(true);
  };

  /**
   * Mở modal ở chế độ sửa
   */
  const openEditModal = (record) => {
    setMode("edit");
    setModalTitle("Sửa nguyên liệu");
    setCurrentId(record.id);

    // Set giá trị lên form
    form.setFieldsValue({
      name: record.name,
      unit: record.unit,
      stockQuantity: record.stockQuantity,
      minStock: record.minStock,
    });

    setIsModalOpen(true);
  };

  /**
   * Xử lý submit form (Thêm / Sửa)
   */
  const handleSubmit = async () => {
    try {
      // Validate form
      const values = await form.validateFields();

      if (mode === "create") {
        await createIngredient(values);
        message.success("Thêm nguyên liệu thành công");
      } else {
        await updateIngredient(currentId, values);
        message.success("Cập nhật nguyên liệu thành công");
      }

      setIsModalOpen(false);
      form.resetFields();
      // Reload lại danh sách
      loadData();
    } catch (error) {
      // Nếu error là do validate thì không show message error ở đây
      if (error?.errorFields) {
        // lỗi validate form
        return;
      }
      console.error(error);
      message.error("Có lỗi xảy ra, vui lòng thử lại");
    }
  };

  /**
   * Xử lý xóa nguyên liệu
   */
  const handleDelete = async (id) => {
    try {
      await deleteIngredient(id);
      message.success("Xóa nguyên liệu thành công");
      loadData();
    } catch (error) {
      console.error(error);
      message.error("Không thể xóa nguyên liệu");
    }
  };

  /**
   * Định nghĩa cột cho bảng nguyên liệu
   */
  const columns = [
    {
      title: "ID",
      dataIndex: "id",
      width: 70,
    },
    {
      title: "Tên nguyên liệu",
      dataIndex: "name",
    },
    {
      title: "Đơn vị",
      dataIndex: "unit",
      width: 100,
    },
    {
      title: "Tồn kho",
      dataIndex: "stockQuantity",
      render: (value, record) => {
        const isLow = record.minStock != null && value < record.minStock;
        return (
          <span>
            {value}{" "}
            {isLow && (
              <Tag color="red">
                Tồn kho thấp
              </Tag>
            )}
          </span>
        );
      },
    },
    {
      title: "Ngưỡng cảnh báo",
      dataIndex: "minStock",
    },
    {
      title: "Người tạo",
      dataIndex: "createdBy",
    },
    {
      title: "Thao tác",
      key: "actions",
      render: (_, record) => (
        <Space>
          <Button type="primary" onClick={() => openEditModal(record)}>
            Sửa
          </Button>

          <Popconfirm
            title="Xóa nguyên liệu"
            description="Bạn có chắc chắn muốn xóa nguyên liệu này không?"
            okText="Xóa"
            cancelText="Hủy"
            okButtonProps={{ danger: true }}
            onConfirm={() => handleDelete(record.id)}
          >
            <Button danger>Xóa</Button>
          </Popconfirm>
        </Space>
      ),
    },
  ];

  return (
    <div>
      {/* Nút thêm nguyên liệu */}
      <div style={{ marginBottom: 16, display: "flex", justifyContent: "flex-end" }}>
        <Button type="primary" onClick={openCreateModal}>
          + Thêm nguyên liệu
        </Button>
      </div>

      {/* Bảng danh sách nguyên liệu */}
      <Table
        rowKey="id"
        columns={columns}
        dataSource={ingredients}
        loading={loading}
      />

      {/* Modal Thêm / Sửa */}
      <Modal
        title={modalTitle}
        open={isModalOpen}
        onOk={handleSubmit}
        onCancel={() => {
          setIsModalOpen(false);
          form.resetFields();
        }}
        okText={mode === "create" ? "Thêm" : "Lưu"}
        cancelText="Hủy"
        destroyOnClose
      >
        <IngredientForm form={form} />
      </Modal>
    </div>
  );
}
