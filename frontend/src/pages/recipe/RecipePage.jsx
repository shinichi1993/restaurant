// ADD NEW FILE: RecipePage.jsx
// Màn hình chính quản lý định lượng món ăn

import { useEffect, useState } from "react";
import {
  Button,
  Table,
  Modal,
  message,
  Form,
  Select,
  Row,
  Col,
  Popconfirm,
} from "antd";

import { useSearchParams } from "react-router-dom";

import {
  getRecipeByDish,
  addRecipeItem,
  updateRecipeItem,
  deleteRecipeItem,
} from "../../api/recipeApi";

import { getDishes } from "../../api/dishApi";
import { getIngredients } from "../../api/ingredientApi";

import RecipeForm from "../../components/recipe/RecipeForm";
import { useAuth } from "../../context/AuthContext";

export default function RecipePage() {
  const [search] = useSearchParams();
  const initialDishId = search.get("dishId");

  const [dishes, setDishes] = useState([]);
  const [ingredients, setIngredients] = useState([]);

  const [dishId, setDishId] = useState(initialDishId || null);
  const [recipeList, setRecipeList] = useState([]);

  const [form] = Form.useForm();
  const [modalOpen, setModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState(null);

  const { accessToken } = useAuth();

  /** Load danh sách món + nguyên liệu */
  useEffect(() => {
    loadIngredients();
    loadDishes();
  }, []);

  const loadDishes = async () => {
    const res = await getDishes();
    setDishes(res.data);
  };

  const loadIngredients = async () => {
    const res = await getIngredients();
    setIngredients(res);
  };

  /** Load định lượng món khi chọn dishId */
  useEffect(() => {
    if (dishId) {
      fetchRecipe();
    }
  }, [dishId]);

  const fetchRecipe = async () => {
    try {
      const res = await getRecipeByDish(dishId);
      setRecipeList(res.data);
    } catch (err) {
      message.error("Không thể tải định lượng");
    }
  };

  /** Bấm nút Thêm */
  const openAddModal = () => {
    setEditingItem(null);
    setModalOpen(true);
    form.resetFields();
  };

  /** Bấm nút Sửa */
  const openEditModal = (record) => {
    setEditingItem(record);
    form.setFieldsValue({
      ingredientId: record.ingredientId,
      quantityNeeded: record.quantityNeeded,
    });
    setModalOpen(true);
  };

  /** Submit modal */
  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();

      if (editingItem) {
        // Update
        await updateRecipeItem(dishId, editingItem.id, values);
        message.success("Cập nhật thành công");
      } else {
        // Add
        await addRecipeItem(dishId, values);
        message.success("Thêm thành công");
      }

      setModalOpen(false);
      fetchRecipe();
    } catch (err) {
      message.error(err.response?.data || "Lỗi khi lưu");
    }
  };

  /** Xoá item */
  const handleDelete = async (id) => {
    try {
      await deleteRecipeItem(dishId, id);
      message.success("Đã xoá");
      fetchRecipe();
    } catch (err) {
      message.error("Không thể xóa");
    }
  };

  // Table columns
  const columns = [
    {
      title: "Nguyên liệu",
      dataIndex: "ingredientName",
    },
    {
      title: "Số lượng (gram/ml)",
      dataIndex: "quantityNeeded",
    },
    {
      title: "Hành động",
      render: (text, record) => (
        <>
          <Button type="primary" onClick={() => openEditModal(record)}>
            Sửa
          </Button>

          <Popconfirm
            title="Xóa nguyên liệu?"
            onConfirm={() => handleDelete(record.id)}
          >
            <Button danger style={{ marginLeft: 8 }}>
              Xóa
            </Button>
          </Popconfirm>
        </>
      ),
    },
  ];

  return (
    <>
      <h2>Định lượng món ăn</h2>

      {/* Chọn món */}
      <Row gutter={16} style={{ marginBottom: 20 }}>
        <Col span={8}>
          <Select
            style={{ width: "100%" }}
            placeholder="Chọn món"
            value={dishId || undefined}
            onChange={(value) => setDishId(value)}
            options={dishes.map((d) => ({
              value: d.id,
              label: d.name,
            }))}
          />
        </Col>
      </Row>

      {/* Bảng định lượng */}
      <Button type="primary" onClick={openAddModal} style={{ marginBottom: 12 }}>
        + Thêm nguyên liệu
      </Button>

      <Table columns={columns} dataSource={recipeList} rowKey="id" />

      {/* Modal thêm / sửa */}
      <Modal
        title={editingItem ? "Sửa định lượng" : "Thêm nguyên liệu"}
        open={modalOpen}
        onOk={handleSubmit}
        onCancel={() => setModalOpen(false)}
        okText="Lưu"
        cancelText="Hủy"
        forceRender   // FIX: để Form reset đúng khi đóng mở modal
      >
        <RecipeForm form={form} ingredients={ingredients || []} />
      </Modal>

    </>
  );
}
