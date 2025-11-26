// pages/dashboard/DashboardPage.jsx
// Trang Dashboard tổng quan hệ thống (Module 09)

import { useEffect, useState } from "react";
import { Card, Row, Col, Typography, Spin } from "antd";
import { Line } from "@ant-design/charts";
import dayjs from "dayjs";
import reportApi from "../api/reportApi";

const { Title } = Typography;

/**
 * Hàm format số tiền (VND)
 */
const formatCurrency = (value) => {
  if (value == null) return "0";
  return value.toLocaleString("vi-VN") + " ₫";
};

/**
 * Hàm format ngày theo Rule 15: dd/MM/yyyy HH:mm
 * Dùng cho phần biểu đồ nếu cần hiển thị
 */
const formatDate = (dateStr) => {
  return dayjs(dateStr).format("DD/MM/YYYY");
};

export default function DashboardPage() {
  // State lưu dữ liệu tổng quan
  const [summary, setSummary] = useState(null);

  // State lưu dữ liệu biểu đồ
  const [dailyData, setDailyData] = useState([]);

  // Loading state
  const [loading, setLoading] = useState(true);

  /**
   * Load dữ liệu Dashboard khi mở trang
   */
  useEffect(() => {
    const loadData = async () => {
      try {
        const [summaryRes, dailyRes] = await Promise.all([
          reportApi.getSummary(),
          reportApi.getDaily(7),
        ]);

        setSummary(summaryRes);
        // Convert dữ liệu daily thành format cho biểu đồ
        setDailyData(
          dailyRes.map((item) => ({
            date: formatDate(item.date), // dd/MM/yyyy
            revenue: Number(item.revenue),
          }))
        );
      } catch (err) {
        console.error("Lỗi load dashboard:", err);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  if (loading) {
    return (
      <div style={{ textAlign: "center", marginTop: 40 }}>
        <Spin size="large" />
      </div>
    );
  }

  return (
    <div>
      {/* Tiêu đề trang */}
      <Title level={2} style={{ marginBottom: 24 }}>
        Dashboard tổng quan
      </Title>

      {/* ===================== 5 CARDS THỐNG KÊ ===================== */}
      <Row gutter={[16, 16]}>
        <Col xs={24} sm={12} md={12} lg={6}>
          <Card title="Tổng doanh thu" bordered={false}>
            <Title level={3}>{formatCurrency(summary.totalRevenue)}</Title>
          </Card>
        </Col>

        <Col xs={24} sm={12} md={12} lg={6}>
          <Card title="Tổng số đơn" bordered={false}>
            <Title level={3}>{summary.totalOrders}</Title>
          </Card>
        </Col>

        <Col xs={24} sm={12} md={12} lg={6}>
          <Card title="Đã thanh toán" bordered={false}>
            <Title level={3}>{summary.paidOrders}</Title>
          </Card>
        </Col>

        <Col xs={24} sm={12} md={12} lg={6}>
          <Card title="Chưa thanh toán" bordered={false}>
            <Title level={3}>{summary.pendingOrders}</Title>
          </Card>
        </Col>

        <Col xs={24} sm={12} md={12} lg={6}>
          <Card title="Doanh thu hôm nay" bordered={false}>
            <Title level={3}>{formatCurrency(summary.todayRevenue)}</Title>
          </Card>
        </Col>
      </Row>

      {/* ===================== LINE CHART DOANH THU ===================== */}
      <Card
        title="Doanh thu 7 ngày gần nhất"
        style={{ marginTop: 32 }}
        bordered={false}
      >
        <Line
          data={dailyData}
          xField="date"
          yField="revenue"
          smooth={true}
          height={300}
          point={{ size: 4 }}
          tooltip={{
            formatter: (datum) => ({
              name: "Doanh thu",
              value: formatCurrency(datum.revenue),
            }),
          }}
        />
      </Card>
    </div>
  );
}
