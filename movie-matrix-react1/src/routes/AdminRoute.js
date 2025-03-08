import { useContext, useEffect } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { AuthContext } from "../context/AuthProvider";

const AdminRoute = () => {
    const { user } = useContext(AuthContext);
  
    useEffect(() => {
      console.log("🔍 AdminRoute rendered");
      console.log("🛑 Checking user:", user);
    }, [user]);
  
    if (user === null) {
      console.log("⏳ Waiting for authentication...");
      return <div>Loading...</div>; // Prevents premature redirection
    }
  
    return user && user.roles?.includes("ADMIN") ? <Outlet /> : <Navigate to="/login" />;
  };
  

export default AdminRoute;
