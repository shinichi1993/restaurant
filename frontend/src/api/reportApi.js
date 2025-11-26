// api/reportApi.js
// API lấy dữ liệu Dashboard (Module 09)

import axiosClient from "./axiosClient";

const reportApi = {
  /**
   * Lấy dữ liệu tổng quan Dashboard
   * - Tổng số đơn
   * - Tổng doanh thu
   * - Số đơn đã thanh toán
   * - Số đơn chưa thanh toán
   * - Doanh thu hôm nay
   * - Số đơn hôm nay
   */
  getSummary() {
    return axiosClient.get("/report/summary").then((res) => res.data);
  },

  /**
   * Lấy dữ liệu doanh thu theo ngày (biểu đồ)
   * @param {number} days số ngày gần nhất
   */
  getDaily(days = 7) {
    return axiosClient
      .get(`/report/daily?days=${days}`)
      .then((res) => res.data);
  },
};

export default reportApi;
