class ApiService {
    constructor(baseURL) {
      this.baseURL = baseURL;
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
  
  export default apiService;
  