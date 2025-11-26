import { Layout, Menu, Button } from "antd";
import { UserOutlined, HomeOutlined, LogoutOutlined } from "@ant-design/icons";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { Outlet } from "react-router-dom";

const { Header, Sider, Content } = Layout;

/**
 * AdminLayout – Layout chính cho module quản trị
 * – Sidebar menu
 * – Header Logout
 */
export default function AdminLayout() {
  const navigate = useNavigate();
  const { logout, user } = useAuth();

  const onLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <Layout style={{ minHeight: "100vh" }}>
      {/* Sidebar */}
      <Sider theme="light" width={200}>
        <Menu mode="inline" style={{ height: "100%", borderRight: 0 }}>
          <Menu.Item key="dashboard" icon={<HomeOutlined />}>
            <Link to="/">Dashboard</Link>
          </Menu.Item>

          <Menu.Item key="users" icon={<UserOutlined />}>
            <Link to="/users">Users</Link>
          </Menu.Item>

          <Menu.Item key="category" icon={<UserOutlined />}>
            <Link to="/category">Category</Link>
          </Menu.Item>

          <Menu.Item key="dish" icon={<UserOutlined />}>
            <Link to="/dish">Món ăn</Link>
          </Menu.Item>

          <Menu.Item key="ingredient" icon={<UserOutlined />}>
            <Link to="/ingredient">Nguyên liệu</Link>
          </Menu.Item>

          <Menu.Item key="stock-entries" icon={<UserOutlined />}>
            <Link to="/stock-entries">Kho</Link>
          </Menu.Item>

          <Menu.Item key="recipe" icon={<UserOutlined />}>
            <Link to="/recipe">Định lượng</Link>
          </Menu.Item>

          <Menu.Item key="orders" icon={<UserOutlined />}>
            <Link to="/orders">Đơn hàng</Link>
          </Menu.Item>

          <Menu.Item key="invoices" icon={<UserOutlined />}>
            <Link to="/invoices">Hóa đơn</Link>
          </Menu.Item>

          <Menu.Item key="payments" icon={<UserOutlined />}>
            <Link to="/payments">Thanh toán</Link>
          </Menu.Item>

          <Menu.Item key="settings" icon={<UserOutlined />}>
            <Link to="/settings">Cài đặt</Link>
          </Menu.Item>

        </Menu>
      </Sider>

      {/* Main content */}
      <Layout>
        <Header
          style={{
            background: "#fff",
            display: "flex",
            justifyContent: "flex-end",
            paddingRight: 20,
          }}
        >
          <span style={{ marginRight: 15 }}>Hello, {user?.fullName}</span>

          <Button
            type="primary"
            danger
            icon={<LogoutOutlined />}
            onClick={onLogout}
          >
            Logout
          </Button>
        </Header>

        <Content style={{ margin: "20px", background: "#fff", padding: 20 }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
}
