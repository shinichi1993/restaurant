// src/components/category/ConfirmDeleteModal.jsx

import { Modal } from "antd";

/**
 * ConfirmDeleteModal
 * - Modal xác nhận xoá danh mục
 * - Dùng chung cho các module CRUD
 * - Không chứa logic xoá, chỉ confirm và gọi callback onOk()
 */
export default function ConfirmDeleteModal({ open, onOk, onCancel }) {
  return (
    <Modal
      open={open}
      title="Xác nhận xoá"
      okText="Xoá"
      okType="danger"
      cancelText="Hủy"
      onOk={onOk}          // ⭐ gọi hàm xoá từ parent
      onCancel={onCancel}  // ⭐ đóng modal
    >
      Bạn có chắc chắn muốn xoá mục này không?
    </Modal>
  );
}
