import axiosClient from "./axiosClient";

/**
 * Lấy danh sách đơn hàng
 */
export const getOrders = async () => {
  const res = await axiosClient.get("/orders");
  return res.data; // Rule 19: luôn trả về data thuần
};

/**
 * Lấy chi tiết 1 đơn
 */
export const getOrderById = async (id) => {
  const res = await axiosClient.get(`/orders/${id}`);
  return res.data;
};

/**
 * Tạo mới đơn hàng
 */
export const createOrder = async (payload) => {
  const res = await axiosClient.post("/orders", payload);
  return res.data;
};

/**
 * Cập nhật đơn hàng
 */
export const updateOrder = async (id, payload) => {
  const res = await axiosClient.put(`/orders/${id}`, payload);
  return res.data;
};

/**
 * Xóa đơn hàng
 */
export const deleteOrder = async (id) => {
  const res = await axiosClient.delete(`/orders/${id}`);
  return res.data;
};

/**
 * Thanh toán đơn hàng và tự động tạo hóa đơn
 * - Gọi POST /orders/{id}/pay
 * - Trả về InvoiceResponse (chi tiết hóa đơn)
 */
export const payOrder = async (orderId) => {
  const response = await axiosClient.post(`/orders/${orderId}/pay`);
  return response.data; // luôn trả data thuần theo Rule 19
};

