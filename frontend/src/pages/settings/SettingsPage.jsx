// src/pages/settings/SettingsPage.jsx
//
// Trang Cài đặt hệ thống (Settings)
// - Cho phép xem & cập nhật các cấu hình của quán ăn
// - Gồm các nhóm:
//   + Thông tin quán ăn
//   + Cấu hình chung
//   + Cấu hình in hóa đơn

import { useEffect, useState } from "react";
import {
  Card,
  Form,
  Input,
  InputNumber,
  Row,
  Col,
  Button,
  message,
  Spin,
  Divider,
} from "antd";

import { getSetting, updateSetting } from "../../api/settingApi";

export default function SettingsPage() {
  // Form instance của Ant Design
  const [form] = Form.useForm();

  // State loading khi load dữ liệu ban đầu
  const [loading, setLoading] = useState(false);

  // State loading khi bấm nút Lưu
  const [saving, setSaving] = useState(false);

  /**
   * useEffect: gọi API lấy cấu hình lần đầu
   */
  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const data = await getSetting();

        // Đổ dữ liệu lên form
        form.setFieldsValue({
          restaurantName: data.restaurantName,
          restaurantAddress: data.restaurantAddress,
          restaurantPhone: data.restaurantPhone,
          restaurantEmail: data.restaurantEmail,
          logoUrl: data.logoUrl,
          vatPercent: data.vatPercent,
          servicePercent: data.servicePercent,
          openingTime: data.openingTime,
          closingTime: data.closingTime,
          tableCount: data.tableCount,
          invoiceHeader: data.invoiceHeader,
          invoiceFooter: data.invoiceFooter,
        });
      } catch (error) {
        console.error("Lỗi khi load cấu hình:", error);
        message.error("Không thể tải cấu hình hệ thống, vui lòng thử lại.");
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [form]);

  /**
   * Xử lý khi submit form (bấm nút Lưu cấu hình)
   */
  const handleSubmit = async (values) => {
    try {
      setSaving(true);

      // Gửi dữ liệu lên backend
      await updateSetting(values);

      message.success("Cập nhật cấu hình thành công.");
    } catch (error) {
      console.error("Lỗi khi cập nhật cấu hình:", error);
      message.error("Cập nhật cấu hình thất bại, vui lòng thử lại.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <div>
      {/* Tiêu đề trang */}
      <h2 style={{ marginBottom: 16 }}>Cài đặt hệ thống</h2>

      {/* Khi đang load dữ liệu ban đầu thì bọc trong Spin */}
      <Spin spinning={loading}>
        <Card>
          <Form
            form={form}
            layout="vertical"
            onFinish={handleSubmit}
          >
            {/* Nhóm: Thông tin quán ăn */}
            <Divider orientation="left">Thông tin quán ăn</Divider>
            <Row gutter={16}>
              <Col xs={24} md={12}>
                <Form.Item
                  label="Tên quán ăn"
                  name="restaurantName"
                  rules={[
                    { required: true, message: "Vui lòng nhập tên quán ăn" },
                  ]}
                >
                  <Input placeholder="Nhập tên quán ăn" />
                </Form.Item>
              </Col>

              <Col xs={24} md={12}>
                <Form.Item
                  label="Địa chỉ"
                  name="restaurantAddress"
                >
                  <Input placeholder="Nhập địa chỉ quán" />
                </Form.Item>
              </Col>

              <Col xs={24} md={12}>
                <Form.Item
                  label="Số điện thoại"
                  name="restaurantPhone"
                >
                  <Input placeholder="Nhập số điện thoại" />
                </Form.Item>
              </Col>

              <Col xs={24} md={12}>
                <Form.Item
                  label="Email liên hệ"
                  name="restaurantEmail"
                >
                  <Input placeholder="Nhập email liên hệ" />
                </Form.Item>
              </Col>

              <Col xs={24} md={12}>
                <Form.Item
                  label="Logo (URL)"
                  name="logoUrl"
                >
                  <Input placeholder="Nhập URL logo quán (nếu có)" />
                </Form.Item>
              </Col>
            </Row>

            {/* Nhóm: Cấu hình chung */}
            <Divider orientation="left">Cấu hình chung</Divider>
            <Row gutter={16}>
              <Col xs={24} md={8}>
                <Form.Item
                  label="Thuế VAT (%)"
                  name="vatPercent"
                >
                  <InputNumber
                    min={0}
                    max={100}
                    style={{ width: "100%" }}
                    placeholder="VD: 8"
                  />
                </Form.Item>
              </Col>

              <Col xs={24} md={8}>
                <Form.Item
                  label="Phí dịch vụ (%)"
                  name="servicePercent"
                >
                  <InputNumber
                    min={0}
                    max={100}
                    style={{ width: "100%" }}
                    placeholder="VD: 5"
                  />
                </Form.Item>
              </Col>

              <Col xs={24} md={8}>
                <Form.Item
                  label="Số bàn"
                  name="tableCount"
                >
                  <InputNumber
                    min={0}
                    style={{ width: "100%" }}
                    placeholder="VD: 10"
                  />
                </Form.Item>
              </Col>

              <Col xs={24} md={12}>
                <Form.Item
                  label="Giờ mở cửa (HH:mm)"
                  name="openingTime"
                >
                  {/* Để đơn giản, dùng Input string thay vì TimePicker để tránh xử lý convert */}
                  <Input placeholder="VD: 08:00" />
                </Form.Item>
              </Col>

              <Col xs={24} md={12}>
                <Form.Item
                  label="Giờ đóng cửa (HH:mm)"
                  name="closingTime"
                >
                  <Input placeholder="VD: 21:00" />
                </Form.Item>
              </Col>
            </Row>

            {/* Nhóm: Cấu hình in hóa đơn */}
            <Divider orientation="left">Cấu hình in hóa đơn</Divider>
            <Row gutter={16}>
              <Col xs={24} md={12}>
                <Form.Item
                  label="Tiêu đề trên hóa đơn"
                  name="invoiceHeader"
                >
                  <Input placeholder="VD: HÓA ĐƠN THANH TOÁN" />
                </Form.Item>
              </Col>

              <Col xs={24} md={12}>
                <Form.Item
                  label="Footer hóa đơn"
                  name="invoiceFooter"
                >
                  <Input placeholder="VD: Cảm ơn quý khách, hẹn gặp lại!" />
                </Form.Item>
              </Col>
            </Row>

            {/* Nút Lưu */}
            <Form.Item style={{ marginTop: 24 }}>
              <Button
                type="primary"
                htmlType="submit"
                loading={saving}
              >
                Lưu cấu hình
              </Button>
            </Form.Item>
          </Form>
        </Card>
      </Spin>
    </div>
  );
}
