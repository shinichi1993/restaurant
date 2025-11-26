// ADD NEW FILE: recipeApi.js
// API gọi đến Backend Recipe
// URL chuẩn backend: /api/dishes/{dishId}/recipes

import api from "./axiosClient";

// Lấy danh sách định lượng theo món
export function getRecipeByDish(dishId) {
  return api.get(`/dishes/${dishId}/recipes`);
}

// Thêm nguyên liệu vào món
export function addRecipeItem(dishId, data) {
  return api.post(`/dishes/${dishId}/recipes`, data);
}

// Cập nhật định lượng
export function updateRecipeItem(dishId, recipeItemId, data) {
  return api.put(`/dishes/${dishId}/recipes/${recipeItemId}`, data);
}

// Xóa nguyên liệu khỏi món
export function deleteRecipeItem(dishId, recipeItemId) {
  return api.delete(`/dishes/${dishId}/recipes/${recipeItemId}`);
}
