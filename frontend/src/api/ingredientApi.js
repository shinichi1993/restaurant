// src/api/ingredientApi.js

import axios from "./axiosClient"; 
// 🔥 Lưu ý: dùng cùng axiosInstance mà bạn đã dùng cho category/dish
// Nếu file axiosInstance nằm chỗ khác (vd: src/api/index.js) thì chỉnh lại import cho đúng.

/**
 * Lấy danh sách nguyên liệu
 */
export const getIngredients = async () => {
  const response = await axios.get("/ingredients");
  return response.data;
};

/**
 * Tạo mới nguyên liệu
 * @param {Object} payload { name, unit, stockQuantity, minStock }
 */
export const createIngredient = async (payload) => {
  const response = await axios.post("/ingredients", payload);
  return response.data;
};

/**
 * Cập nhật nguyên liệu
 * @param {number} id 
 * @param {Object} payload 
 */
export const updateIngredient = async (id, payload) => {
  const response = await axios.put(`/ingredients/${id}`, payload);
  return response.data;
};

/**
 * Xóa nguyên liệu
 * @param {number} id 
 */
export const deleteIngredient = async (id) => {
  const response = await axios.delete(`/ingredients/${id}`);
  return response.data;
};
