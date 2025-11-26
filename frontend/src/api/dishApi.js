// src/api/dishApi.js

import axiosClient from "./axiosClient";

/**
 * API tạo món ăn mới
 */
export const createDish = (data) => {
  return axiosClient.post("/dish", data);
};

/**
 * API lấy danh sách tất cả món ăn
 */
export const getDishes = () => {
  return axiosClient.get("/dish");
};

/**
 * API lấy danh sách món theo category
 */
export const getDishesByCategory = (categoryId) => {
  return axiosClient.get(`/dish/category/${categoryId}`);
};

/**
 * API cập nhật món ăn
 */
export const updateDish = (id, data) => {
  return axiosClient.put(`/dish/${id}`, data);
};

/**
 * API xoá món ăn
 */
export const deleteDish = (id) => {
  return axiosClient.delete(`/dish/${id}`);
};
