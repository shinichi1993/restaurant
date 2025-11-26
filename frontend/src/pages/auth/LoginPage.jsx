import { useState } from "react";
import { Card, Form, Input, Button, message } from "antd";
import axios from "../../api/axiosClient";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";

/**
 * Trang Login – gọi API /auth/login
 */
export default function LoginPage() {
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const onFinish = async (values) => {
    try {
      setLoading(true);
      const resp = await axios.post("/auth/login", values);
      login(resp.data);
      message.success("Đăng nhập thành công!");
      navigate("/");
    } catch (e) {
      message.error("Sai email hoặc mật khẩu");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div
      style={{
        width: 400,
        margin: "100px auto",
      }}
    >
      <Card title="Đăng nhập">
        <Form layout="vertical" onFinish={onFinish}>
          <Form.Item
            label="Email"
            name="email"
            rules={[{ required: true, message: "Nhập email" }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            label="Mật khẩu"
            name="password"
            rules={[{ required: true, message: "Nhập mật khẩu" }]}
          >
            <Input.Password />
          </Form.Item>

          <Button type="primary" htmlType="submit" block loading={loading}>
            Đăng nhập
          </Button>
        </Form>
      </Card>
    </div>
  );
}
