class ApiServices {
    constructor(baseURL) {
      this.baseURL = baseURL;
      console.l
    }
  

//     import apiService from "../services/ApiService";

// apiService.get("/movies").then((data) => console.log(data));
    getHeaders() {
      const token = localStorage.getItem("token");
      return {
        Authorization: token ? `Bearer ${token}` : "",
        "Content-Type": "application/json",
      };
    }
  
    async request(endpoint, method = "GET", body = null) {
      const options = {
        method,
        headers: this.getHeaders(),
      };
  
      if (body) {
        options.body = JSON.stringify(body);
      }
  
      try {
        const response = await fetch(`${this.baseURL}${endpoint}`, options);
        if (!response.ok) {
          throw new Error(`API Error: ${response.statusText}`);
        }
        return await response.json();
      } catch (error) {
        console.error("API Request Failed:", error);
        throw error;
      }
    }
  
    get(endpoint) {
      return this.request(endpoint, "GET");
    }
  
    post(endpoint, data) {
      return this.request(endpoint, "POST", data);
    }
  
    put(endpoint, data) {
      return this.request(endpoint, "PUT", data);
    }
  
    delete(endpoint) {
      return this.request(endpoint, "DELETE");
    }
  }
  
  const apiService = new ApiService("http://localhost:8080/api"); // Set your backend API URL
  
  export default apiServices;
  

// sotre access token and refresh token

//   import axios from "axios";

// const apiService = axios.create({
//     baseURL: "http://localhost:8080/api",
// });

// // ✅ Intercept requests to attach access token
// apiService.interceptors.request.use((config) => {
//     const token = localStorage.getItem("accessToken");
//     if (token) config.headers.Authorization = `Bearer ${token}`;
//     return config;
// });

// // ✅ Intercept responses to refresh token on 401 error
// apiService.interceptors.response.use(
//     (response) => response,
//     async (error) => {
//         if (error.response.status === 401) {
//             try {
//                 const refreshToken = localStorage.getItem("refreshToken");
//                 const res = await axios.post("http://localhost:8080/api/refresh", { refreshToken });

//                 localStorage.setItem("accessToken", res.data.accessToken);
//                 localStorage.setItem("refreshToken", res.data.refreshToken);

//                 // ✅ Retry the original request
//                 error.config.headers.Authorization = `Bearer ${res.data.accessToken}`;
//                 return axios(error.config);
//             } catch (refreshError) {
//                 console.error("Refresh token failed", refreshError);
//                 localStorage.removeItem("accessToken");
//                 localStorage.removeItem("refreshToken");
//                 window.location.href = "/login"; // ✅ Force login
//             }
//         }
//         return Promise.reject(error);
//     }
// );

// export default apiService;
