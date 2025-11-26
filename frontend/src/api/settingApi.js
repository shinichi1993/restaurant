// src/api/settingApi.js
// API gọi lên backend để lấy / cập nhật cấu hình hệ thống

import axiosClient from "./axiosClient";

/**
 * Lấy cấu hình hệ thống hiện tại
 * - Gọi GET /settings
 * - Trả về dữ liệu thuần (response.data)
 */
export const getSetting = async () => {
  const res = await axiosClient.get("/settings");
  return res.data; // Rule 19: luôn trả về res.data
};

/**
 * Cập nhật cấu hình hệ thống
 * - Gọi PUT /settings
 * - body: SettingRequest (xem backend)
 */
export const updateSetting = async (payload) => {
  const res = await axiosClient.put("/settings", payload);
  return res.data; // Trả về SettingResponse
};
