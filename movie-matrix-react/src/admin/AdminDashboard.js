import React from "react";
import { Admin, Resource } from "react-admin";
import dataProvider from "./dataProvider";
import MovieList from "./MovieList";
import MovieEdit from "./MovieEdit";
import MovieCreate from "./MovieCreate";
import AdminWelcome from "./AdminWelcome";
import AdminLayout from "./AdminLayout";
import CustomMenu from "./CustomMenu"; // Import the custom menu
import { AuthContext } from "../context/AuthProvider";
import { useEffect,useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import UserList from "./UserList";
import UserEdit from "./UserEdit";

const AdminDashboard = () => {
  const { authProvider } = useContext(AuthContext);
  const navigate = useNavigate();
  const [isAuthenticated, setIsAuthenticated] = useState(null);

  useEffect(() => {
      authProvider.checkAuth()
          .then(() => {
              console.log("✅ User authenticated");
              setIsAuthenticated(true);
          })
          .catch(() => {
              console.log("❌ User not authenticated. Redirecting...");
              setIsAuthenticated(false);
              navigate("/login", { replace: true });
          });
  }, [authProvider, navigate]);

  if (isAuthenticated === null) {
      return <p>Loading...</p>; // Show a loading message while checking auth
  }
  return (
    <Admin 
    authProvider={authProvider}
      dataProvider={dataProvider} 
      dashboard={AdminWelcome} 
      layout={AdminLayout}
      menu={CustomMenu} 
      basename="/admin"
    >
      <Resource name="movies" list={MovieList} edit={MovieEdit} create={MovieCreate} />
      <Resource name="user" list={UserList} edit={UserEdit} />
    </Admin>
  );
};

export default AdminDashboard;
