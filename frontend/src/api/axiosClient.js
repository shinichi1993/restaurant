import axios from "axios";

const axiosClient = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
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
