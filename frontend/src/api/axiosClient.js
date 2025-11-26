import axios from "axios";

const axiosClient = axios.create({
  baseURL: "/api",
});

// Gắn token vào header
axiosClient.interceptors.request.use((config) => {
  const accessToken = localStorage.getItem("accessToken");  // ⭐ KHÔNG PARSE, KHÔNG SESSION

  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }

  return config;
});

export default axiosClient;
