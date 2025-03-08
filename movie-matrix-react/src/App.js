import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Home from "./pages/Home";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import AdminDashboard from "./admin/AdminDashboard";
import AuthProvider from "./context/AuthProvider";
import AdminRoute from "./routes/AdminRoute"; // Protect Admin Routes
import UserRoute from "./routes/UserRoute"; // Protect User Routes
import UserDashboard from "./pages/UserDashboard";
// import ProfilePage from "./pages/ProfilePage";
import ProfilePage from "./pages/ProfilePage";
// import AdminWelcome from "./admin/AdminWelcome";

const App = () => {
  return (
    <Router>
      <AuthProvider>
        <Navbar />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/movies" element={<h2>Movies Page</h2>} />
          <Route path="/register" element={<Register />} />
          <Route path="/login" element={<Login />} />
          {/* <Route path="/profile" element={<ProfilePage />} /> */}
          <Route path="/profile" element={<ProfilePage />} />

          {/* Protected User Routes */}
          <Route element={<UserRoute />}>
            <Route path="/dashboard" element={<UserDashboard />} />
          </Route>

          {/* Protected Admin Routes */}
          <Route element={<AdminRoute />}>
            {/* <Route path="/admin-dashboard" element={<AdminWelcome />} /> */}
            <Route path="/admin/*" element={<AdminDashboard />} />
          </Route>
        </Routes>
        <Footer />
      </AuthProvider>
    </Router>
  );
};

export default App;
