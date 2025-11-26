// src/api/categoryApi.js

import axiosClient from "./axiosClient";

/**
 * API tạo danh mục mới
 */
export const createCategory = (data) => {
  return axiosClient.post("/categories", data);
};

/**
 * API lấy danh sách danh mục
 */
export const getCategories = () => {
  return axiosClient.get("/categories");
};

/**
 * API cập nhật danh mục
 */
export const updateCategory = (id, data) => {
  return axiosClient.put(`/categories/${id}`, data);
};

/**
 * API xóa danh mục theo ID
 */
export const deleteCategory = (id) => {
  return axiosClient.delete(`/categories/${id}`);
};
