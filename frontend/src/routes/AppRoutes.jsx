import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "../pages/auth/LoginPage";
import DashboardPage from "../pages/DashboardPage";
import UserListPage from "../pages/users/UserListPage";
import CategoryPage from "../pages/category/CategoryPage";
import DishPage from "../pages/dish/DishPage";
import IngredientPage from "../pages/ingredient/IngredientPage";
import StockEntryPage from "../pages/stock/StockEntryPage";
import RecipePage from "../pages/recipe/RecipePage";
import OrderPage from "../pages/order/OrderPage";
import InvoicePage from "../pages/invoice/InvoicePage";
import PaymentPage from "../pages/payment/PaymentPage";
// ADD vào nhóm import Page
import SettingsPage from "../pages/settings/SettingsPage";

import PrivateRoute from "./PrivateRoute";
import AdminLayout from "../layouts/AdminLayout";

export default function AppRoutes() {
  return (
    <BrowserRouter>
      <Routes>

        {/* Public */}
        <Route path="/login" element={<LoginPage />} />

        {/* Private – bọc toàn bộ AdminLayout DUY NHẤT MỘT LẦN */}
        <Route
          path="/"
          element={
            <PrivateRoute>
              <AdminLayout />
            </PrivateRoute>
          }
        >

          {/* Các page sẽ render vào {children} của AdminLayout */}
          <Route index element={<DashboardPage />} />
          <Route path="users" element={<UserListPage />} />
          <Route path="category" element={<CategoryPage />} />
          <Route path="dish" element={<DishPage />} />
          <Route path="ingredient" element={<IngredientPage />} />
          <Route path="stock-entries" element={<StockEntryPage />} />
          {/* Module 05 – Recipe */}
          <Route path="/recipe" element={<RecipePage />} />
          {/* ⭐ MODULE 06 — Order */}
          <Route path="/orders" element={<OrderPage />} />
          {/* ⭐ Thêm route hóa đơn */}
          <Route path="/invoices" element={<InvoicePage />} />
          {/* ADD route mới cho Payment, đặt gần route Invoice */}
          <Route path="/payments" element={<PaymentPage />} />
          {/* ... các route khác ... */}
          <Route path="/settings" element={<SettingsPage />} /> {/* ADD: Route Cài đặt */}

        </Route>

        {/* Fallback */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
