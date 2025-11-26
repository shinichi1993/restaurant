// src/api/invoiceApi.js
// API cho Module 07 – Invoice
// Tất cả API đều dùng axiosClient (Rule 18) và return response.data (Rule 19)

import axiosClient from "./axiosClient";

/**
 * Lấy danh sách hóa đơn
 */
export const getInvoices = async () => {
  const response = await axiosClient.get("/invoices");
  return response.data; // trả dữ liệu thuần
};

/**
 * Lọc hóa đơn theo ngày + keyword
 */
export const filterInvoices = async (filter) => {
  const response = await axiosClient.post("/invoices/filter", filter);
  return response.data;
};

/**
 * Lấy chi tiết hóa đơn
 */
export const getInvoiceDetail = async (id) => {
  const response = await axiosClient.get(`/invoices/${id}`);
  return response.data;
};

/**
 * Tạo hóa đơn từ Order
 */
export const createInvoiceFromOrder = async (orderId) => {
  const response = await axiosClient.post(
    `/invoices/create-from-order/${orderId}`
  );
  return response.data;
};

/**
 * Tải file PDF hóa đơn
 */
export const downloadInvoicePdf = async (id) => {
  const response = await axiosClient.get(`/invoices/${id}/pdf`, {
    responseType: "blob", // tải file dạng nhị phân
  });
  return response.data;
};

/**
 * Thanh toán hóa đơn
 * Gọi POST /invoices/{id}/pay
 */
export const payInvoice = async (id) => {
  const response = await axiosClient.post(`/invoices/${id}/pay`);
  return response.data; // luôn return data thuần (Rule 19)
};

