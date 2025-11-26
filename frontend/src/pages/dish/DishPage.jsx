// src/pages/dish/DishPage.jsx

import { useEffect, useState } from "react";
import {
  Button,
  Modal,
  Table,
  message,
  Form,
  Select,
  Row,
  Col,
} from "antd";
import dayjs from "dayjs";

import {
  getDishes,
  getDishesByCategory,
  createDish,
  updateDish,
  deleteDish,
} from "../../api/dishApi";

import { getCategories } from "../../api/categoryApi";

import DishForm from "../../components/dish/DishForm";
import ConfirmDeleteModal from "../../components/category/ConfirmDeleteModal";

/**
 * Trang quản lý món ăn
 * - Danh sách món (Table)
 * - Lọc theo danh mục
 * - Thêm / Sửa / Xóa món
 */
export default function DishPage() {
  /** State danh sách món */
  const [dishes, setDishes] = useState([]);

  /** Danh mục để filter và để chọn trong Form */
  const [categories, setCategories] = useState([]);

  /** Loading */
  const [loading, setLoading] = useState(false);

  /** Modal states */
  const [openCreate, setOpenCreate] = useState(false);
  const [openUpdate, setOpenUpdate] = useState(false);
  const [openDelete, setOpenDelete] = useState(false);

  /** Form */
  const [createForm] = Form.useForm();
  const [updateForm] = Form.useForm();

  /** Item được chọn để sửa hoặc xoá */
  const [selectedDish, setSelectedDish] = useState(null);

  /** Filter category */
  const [filterCategory, setFilterCategory] = useState(null);

  /**
   * Load danh mục
   */
  const loadCategories = async () => {
    try {
      const res = await getCategories();
      setCategories(res.data);
    } catch (err) {
      message.error("Không thể tải danh mục");
    }
  };

  /**
   * Load danh sách món
   */
  const loadDishes = async () => {
    setLoading(true);

    try {
      let res;

      if (filterCategory) {
        res = await getDishesByCategory(filterCategory);
      } else {
        res = await getDishes();
      }

      setDishes(res.data ?? []);
    } catch (err) {
      message.error("Không thể tải danh sách món");
    }

    setLoading(false);
  };

  useEffect(() => {
    loadCategories();
  }, []);

  // Khi filter thay đổi -> load lại danh sách món
  useEffect(() => {
    loadDishes();
    // eslint-disable-next-line
  }, [filterCategory]);

  /**
   * Tạo món ăn mới
   */
  const handleCreate = async (values) => {
    try {
      await createDish(values);
      message.success("Tạo món thành công");

      createForm.resetFields();
      setOpenCreate(false);

      loadDishes();
    } catch (err) {
      message.error("Không thể tạo món");
    }
  };

  /**
   * Cập nhật món ăn
   */
  const handleUpdate = async (values) => {
    try {
      await updateDish(selectedDish.id, values);
      message.success("Cập nhật món thành công");

      updateForm.resetFields();
      setOpenUpdate(false);

      loadDishes();
    } catch (err) {
      message.error("Không thể cập nhật món");
    }
  };

  /**
   * Xoá món
   */
  const handleDelete = async () => {
    try {
      await deleteDish(selectedDish.id);
      message.success("Xóa món thành công");

      setOpenDelete(false);
      loadDishes();
    } catch (err) {
      message.error("Không thể xóa món");
    }
  };

  /**
   * Cấu hình bảng món ăn
   */
  const columns = [
    {
      title: "Tên món",
      dataIndex: "name",
    },
    {
      title: "Mô tả",
      dataIndex: "description",
    },
    {
      title: "Giá bán (VNĐ)",
      dataIndex: "price",
      render: (val) => val?.toLocaleString(),
    },
    {
      title: "Danh mục",
      dataIndex: "categoryName",
    },
    {
      title: "Ngày tạo",
      dataIndex: "createdAt",
      render: (value) =>
        value ? dayjs(value).format("DD/MM/YYYY HH:mm") : "",
    },
    {
      title: "Hành động",
      render: (_, record) => (
        <>
          {/* Nút sửa */}
          <Button
            type="primary"
            style={{ marginRight: 8 }}
            onClick={() => {
              setSelectedDish(record);
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
              setSelectedDish(record);
              setOpenDelete(true);
            }}
          >
            Xóa
          </Button>
        </>
      ),
    },
    {
      title: "Định lượng",
      render: (_, record) => (
        <Button
          type="primary"
          onClick={() => navigate(`/recipe?dishId=${record.id}`)}
        >
          Định lượng
        </Button>
      )
    }

  ];

  return (
    <div style={{ padding: 20 }}>
      <h2>Quản lý món ăn</h2>

      {/* Filter + Nút thêm */}
      <Row justify="space-between" style={{ marginBottom: 16 }}>
        <Col>
          <Select
            placeholder="Lọc theo danh mục"
            allowClear
            style={{ width: 250 }}
            value={filterCategory}
            onChange={(v) => setFilterCategory(v)}
          >
            {categories.map((c) => (
              <Select.Option key={c.id} value={c.id}>
                {c.name}
              </Select.Option>
            ))}
          </Select>
        </Col>

        <Col>
          <Button
            type="primary"
            onClick={() => setOpenCreate(true)}
            style={{ background: "#1677ff" }}
          >
            + Thêm món
          </Button>
        </Col>
      </Row>

      {/* Table */}
      <Table
        rowKey="id"
        loading={loading}
        columns={columns}
        dataSource={dishes}
      />

      {/* Modal Create */}
      <Modal
        title="Thêm món ăn"
        open={openCreate}
        onOk={() => createForm.submit()}
        onCancel={() => {
          createForm.resetFields();
          setOpenCreate(false);
        }}
        okText="Thêm"
        cancelText="Hủy"
      >
        <DishForm form={createForm} categories={categories} onFinish={handleCreate}/>
      </Modal>

      {/* Modal Update */}
      <Modal
        title="Cập nhật món ăn"
        open={openUpdate}
        onOk={() => updateForm.submit()}
        onCancel={() => {
          updateForm.resetFields();
          setOpenUpdate(false);
        }}
        okText="Cập nhật"
        cancelText="Hủy"
      >
        <DishForm form={updateForm} categories={categories} onFinish={handleUpdate}/>
      </Modal>

      {/* Modal Delete */}
      <ConfirmDeleteModal
        open={openDelete}
        onOk={handleDelete}
        onCancel={() => setOpenDelete(false)}
      />
    </div>
  );
}
