// src/api/paymentApi.js
// API gọi lên backend Payment
// LƯU Ý: dùng axiosClient với baseURL "/api", KHÔNG thêm "/api" vào path

import axiosClient from "./axiosClient";

/**
 * Lấy danh sách payment với filter + phân trang.
 * params:
 *  - fromDate, toDate: string "YYYY-MM-DD" (có thể undefined)
 *  - method: CASH / MOMO / BANK (có thể undefined)
 *  - status: SUCCESS / FAILED / PENDING (có thể undefined)
 *  - page: số trang (0-based)
 *  - size: số bản ghi mỗi trang
 *
 * Trả về: Page<PaymentResponse> từ backend (content, totalElements, ...).
 */
export async function fetchPayments(params) {
  const response = await axiosClient.get("/payments", { params });
  // Rule 19: luôn trả về response.data
  return response.data;
}

/**
 * Lấy chi tiết 1 payment theo id.
 */
export async function getPaymentDetail(id) {
  const response = await axiosClient.get(`/payments/${id}`);
  return response.data;
}

/**
 * Tạo payment cho 1 invoice.
 * Tạm thời chưa dùng ở UI (sẽ dùng khi tích hợp thanh toán online),
 * nhưng khai báo sẵn để về sau dùng lại.
 */
export async function createPayment(invoiceId, data) {
  const response = await axiosClient.post(`/payments/${invoiceId}`, data);
  return response.data;
}
