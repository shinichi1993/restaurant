import { createContext, useContext, useState } from "react";

/**
 * AuthContext – Lưu accessToken, refreshToken, user
 */
const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [accessToken, setAccessToken] = useState(
    localStorage.getItem("accessToken")
  );

  const [refreshToken, setRefreshToken] = useState(
    localStorage.getItem("refreshToken")
  );

  const [user, setUser] = useState(
    JSON.parse(localStorage.getItem("user") || "null")
  );

  // Lưu token khi login
  const login = (resp) => {
    setAccessToken(resp.accessToken);
    setRefreshToken(resp.refreshToken);
    setUser(resp);

    localStorage.setItem("accessToken", resp.accessToken);
    localStorage.setItem("refreshToken", resp.refreshToken);
    localStorage.setItem("user", JSON.stringify(resp));
  };

  // Logout – clear storage
  const logout = () => {
    setAccessToken(null);
    setRefreshToken(null);
    setUser(null);

    localStorage.clear();
  };

  return (
    <AuthContext.Provider
      value={{ accessToken, refreshToken, user, login, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  return useContext(AuthContext);
}
