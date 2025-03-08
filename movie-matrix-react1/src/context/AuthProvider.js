import React, { createContext, useState, useEffect, useCallback } from "react";
import { useNavigate } from "react-router-dom";
import { jwtDecode } from "jwt-decode"; // Import jwt-decode

export const AuthContext = createContext();

const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const navigate = useNavigate();

  // Function to decode JWT token
  const decodeToken = (token) => {
    try {
      return jwtDecode(token);
    } catch (error) {
      console.error("Invalid token:", error);
      return null;
    }
  };

  // Logout function wrapped in useCallback to prevent unnecessary re-renders
  const logout = useCallback(() => {
    localStorage.removeItem("token"); // Clear token from local storage
    sessionStorage.clear(); // Clear all session storage
    setUser(null);
    navigate("/", { replace: true });
  }, [navigate]); // ✅ Include navigate as a dependency

  // Check if user is logged in (on page refresh)
  useEffect(() => {
    const token = localStorage.getItem("token");
    console.log("🔄 Checking auth, token found:", token);

    if (token) {
      const decodedUser = decodeToken(token);
      console.log("✅ Decoded user:", decodedUser);

      if (decodedUser) {
        console.log("ℹ️ Setting user:", decodedUser);
        setUser(decodedUser);
      } else {
        console.log("❌ Invalid token. Logging out...");
        logout(); // ✅ Now it's safe to use logout inside useEffect
      }
    } else {
      console.log("❌ No token found. Redirecting to login...");
    }
  }, [logout]); // ✅ Now logout is a stable dependency

  // Login function
  const login = (token) => {
    console.log("Received Token:", token); // Debug: Check token
    localStorage.setItem("token", token);
    const decodedUser = decodeToken(token);
    console.log("Decoded User:", decodedUser); // Debug: Check decoded user

    if (decodedUser) {
      setUser(decodedUser);

      // Redirect based on role after setting user state
      if (decodedUser.role === "ADMIN" || (decodedUser.roles && decodedUser.roles.includes("ADMIN"))) {
        console.log("Admin detected, navigating to /admin/movies");
        navigate("/admin", { replace: true });
      } else {
        console.log("User detected, navigating to /dashboard");
        navigate("/dashboard", { replace: true });
      }
    } else {
      console.error("Error: Decoded user is null, login failed");
    }
  };

  // React-Admin Authentication Provider
  const authProvider = {
    login: async ({ username, password }) => {
      try {
        const response = await fetch("http://localhost:8080/api/auth/login", {
          method: "POST",
          body: JSON.stringify({ email: username, password }),
          headers: { "Content-Type": "application/json" },
        });

        const data = await response.json();
        if (!response.ok) throw new Error(data.message || "Invalid credentials");

        if (data.token) {
          login(data.token);
          return Promise.resolve();
        }

        return Promise.reject("Invalid credentials");
      } catch (error) {
        return Promise.reject(error.message);
      }
    },

    logout: () => {
      logout();
      return Promise.resolve();
    },

    checkAuth: () => {
      const token = localStorage.getItem("token");
      console.log("🔍 checkAuth() triggered. Token:", token);

      if (token) {
        try {
          const decodedUser = jwtDecode(token);
          console.log("✅ checkAuth() Decoded user:", decodedUser);

          // Ensure the user is an ADMIN for admin dashboard access
          if (decodedUser.role === "ADMIN" || (decodedUser.roles && decodedUser.roles.includes("ADMIN"))) {
            console.log("✅ checkAuth() passed: User is ADMIN");
            return Promise.resolve();
          } else {
            console.log("🚫 checkAuth() failed: User is not ADMIN");
            return Promise.reject();
          }
        } catch (error) {
          console.error("❌ checkAuth() error decoding token:", error);
          return Promise.reject();
        }
      }

      console.log("❌ checkAuth() failed: No token found");
      return Promise.reject();
    },

    getIdentity: async () => {
      const token = localStorage.getItem("token");
      if (!token) return Promise.reject();

      const decodedUser = decodeToken(token);
      return Promise.resolve({
        id: decodedUser.sub,
        fullName: decodedUser.name,
        role: decodedUser.role,
      });
    },

    getPermissions: async () => {
      const token = localStorage.getItem("token");
      if (!token) return Promise.reject();

      const decodedUser = decodeToken(token);
      return Promise.resolve(decodedUser.role); // React-Admin expects a string, not an array
    },
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, authProvider }}>
      {children}
    </AuthContext.Provider>
  );
};

export default AuthProvider;
