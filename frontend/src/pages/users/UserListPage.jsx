import { useEffect, useState } from "react";
import { Table, Button, message, Popconfirm } from "antd";
import AdminLayout from "../../layouts/AdminLayout";
import axiosClient from "../../api/axiosClient";
import AddUserModal from "./AddUserModal";
import EditUserModal from "./EditUserModal";

/**
 * Danh sách user + Add + Edit + Delete
 */
export default function UserListPage() {
  const [loading, setLoading] = useState(false);
  const [users, setUsers] = useState([]);

  const [openAddModal, setOpenAddModal] = useState(false);
  const [openEditModal, setOpenEditModal] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);

  // API load user list
  const loadUsers = async () => {
    try {
      setLoading(true);
      const resp = await axiosClient.get("/users");
      setUsers(resp.data);
    } catch (e) {
      message.error("Không tải được danh sách user");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadUsers();
  }, []);

  // API delete user
  const deleteUser = async (id) => {
    try {
      await axiosClient.delete(`/users/${id}`);
      message.success("Xoá user thành công!");
      loadUsers();
    } catch (err) {
      message.error("Không thể xoá user");
    }
  };

  const columns = [
    { title: "ID", dataIndex: "id", width: 70 },
    { title: "Full Name", dataIndex: "fullName" },
    { title: "Email", dataIndex: "email" },
    { title: "Phone", dataIndex: "phone" },

    {
      title: "Actions",
      width: 200,
      render: (_, record) => (
        <div style={{ display: "flex", gap: 10 }}>

          {/* Nút Edit */}
          <Button
            type="primary"
            onClick={() => {
              setSelectedUser(record);
              setOpenEditModal(true);
            }}
          >
            Sửa
          </Button>

          {/* Nút Delete với confirm */}
          <Popconfirm
            title="Bạn có chắc muốn xoá user này?"
            onConfirm={() => deleteUser(record.id)}
            okText="Xoá"
            cancelText="Hủy"
          >
            <Button danger>Xóa</Button>
          </Popconfirm>

        </div>
      ),
    },
  ];

  return (
    <div>
      <div style={{ display: "flex", justifyContent: "space-between", marginBottom: 15 }}>
        <h2>User Management</h2>

        <Button type="primary" onClick={() => setOpenAddModal(true)}>
          Add User
        </Button>
      </div>

      <Table
        columns={columns}
        dataSource={users}
        rowKey="id"
        loading={loading}
        bordered
      />

      {/* Modal thêm user */}
      <AddUserModal
        open={openAddModal}
        onClose={() => setOpenAddModal(false)}
        onSuccess={loadUsers}
      />

      {/* Modal sửa user */}
      <EditUserModal
        open={openEditModal}
        onClose={() => setOpenEditModal(false)}
        onSuccess={loadUsers}
        user={selectedUser}
      />
    </div>
  );
}
