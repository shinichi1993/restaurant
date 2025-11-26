import axios from "./axiosClient";

/**
 * Lấy danh sách phiếu nhập kho
 */
export const getStockEntries = () => {
  return axios.get("/stock-entries");
};

/**
 * Tạo phiếu nhập kho mới
 */
export const createStockEntry = (data) => {
  return axios.post("/stock-entries", data);
};
