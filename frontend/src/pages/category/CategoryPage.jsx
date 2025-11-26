// src/pages/category/CategoryPage.jsx

import { useEffect, useState } from "react";
import { Button, Table, Modal, Form, message } from "antd";

import {
  getCategories,
  createCategory,
  updateCategory,
  deleteCategory,
} from "../../api/categoryApi";

import CategoryForm from "../../components/category/CategoryForm";
import ConfirmDeleteModal from "../../components/category/ConfirmDeleteModal";
import dayjs from "dayjs";

/**
 * CategoryPage
 * - Quản lý danh mục
 * - Gồm: Table + Modal Create + Modal Update + Modal Delete
 */
export default function CategoryPage() {
  // Danh sách category
  const [categories, setCategories] = useState([]);

  // Trạng thái loading khi load dữ liệu
  const [loading, setLoading] = useState(false);

  // Form cho modal Create và Update
  const [createForm] = Form.useForm();
  const [updateForm] = Form.useForm();

  // State mở/đóng modal
  const [openCreate, setOpenCreate] = useState(false);
  const [openUpdate, setOpenUpdate] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);

  // Category đang được chọn để update/delete
  const [selected, setSelected] = useState(null);

  /**
   * Load danh sách categories từ API
   */
  const fetchData = async () => {
    setLoading(true);
    try {
      const res = await getCategories();
      setCategories(res.data); // Backend trả về List<CategoryResponse>
    } catch (err) {
      message.error("Lỗi khi tải danh mục");
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchData();
  }, []);

  /**
   * Xử lý tạo danh mục
   */
  const handleCreate = async (values) => {
    try {
      await createCategory(values);
      message.success("Tạo danh mục thành công");
      setOpenCreate(false);
      createForm.resetFields();
      fetchData();
    } catch {
      message.error("Lỗi khi tạo danh mục");
    }
  };

  /**
   * Xử lý cập nhật danh mục
   */
  const handleUpdate = async (values) => {
    try {
      await updateCategory(selected.id, values);
      message.success("Cập nhật danh mục thành công");
      setOpenUpdate(false);
      updateForm.resetFields();
      fetchData();
    } catch {
      message.error("Lỗi khi cập nhật danh mục");
    }
  };

  /**
   * Xử lý xóa danh mục
   */
  const handleDelete = async () => {
    try {
      await deleteCategory(selected.id);
      message.success("Xóa danh mục thành công");
      setOpenDelete(false);
      fetchData();
    } catch {
      message.error("Không thể xóa danh mục");
    }
  };

  /**
   * Cấu hình cột cho bảng
   */
  const columns = [
    {
      title: "Tên danh mục",
      dataIndex: "name",
      key: "name",
    },
    {
      title: "Mô tả",
      dataIndex: "description",
      key: "description",
    },
    {
      title: "Ngày tạo",
      dataIndex: "createdAt",
      key: "createdAt",
      render: (value) => value ? dayjs(value).format("DD/MM/YYYY HH:mm") : "",
    },
    {
      title: "Hành động",
      key: "action",
      render: (_, record) => (
        <>
        <div style={{ display: "flex", gap: 10 }}>
          {/* Nút sửa */}
          <Button
            type="primary"
            onClick={() => {
              setSelected(record);
              updateForm.setFieldsValue(record);
              setOpenUpdate(true);
            }}
          >
            Sửa
          </Button>

          {/* Nút xoá */}
          <Button
            type="primary"
            danger
            onClick={() => {
              setSelected(record);
              setOpenDelete(true);
            }}
          >
            Xóa
          </Button>
          </div>
        </>
      ),
    },
  ];

  return (
    <div style={{ padding: 20 }}>
      <h2>Quản lý danh mục món</h2>

      {/* Nút mở modal tạo danh mục */}
      <Button type="primary" onClick={() => setOpenCreate(true)}>
        + Thêm danh mục
      </Button>

      {/* Bảng danh mục */}
      <Table
        style={{ marginTop: 16 }}
        rowKey="id"
        columns={columns}
        dataSource={categories}
        loading={loading}
      />

      {/* Modal: Thêm danh mục */}
      <Modal
        open={openCreate}
        title="Thêm danh mục"
        onCancel={() => {
          createForm.resetFields();
          setOpenCreate(false);
        }}
        onOk={() => createForm.submit()}
      >
        <CategoryForm form={createForm} onFinish={handleCreate} />
      </Modal>

      {/* Modal: Cập nhật danh mục */}
      <Modal
        open={openUpdate}
        title="Cập nhật danh mục"
        onCancel={() => {
          updateForm.resetFields();
          setOpenUpdate(false);
        }}
        onOk={() => updateForm.submit()}
      >
        <CategoryForm form={updateForm} onFinish={handleUpdate} />
      </Modal>

      {/* Modal: Xác nhận xoá */}
      <ConfirmDeleteModal
        open={openDelete}
        onOk={handleDelete}
        onCancel={() => setOpenDelete(false)}
      />
    </div>
  );
}
