import React, { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthProvider";
import "../styles/Home.css"; // ✅ Import CSS file

const Home = () => {
  const navigate = useNavigate(); // Hook to navigate to other pages
  const { user } = useContext(AuthContext);

  return (
    <div className="home-container">
      <h1 className="home-title">Welcome to Movie Matrix 🎬</h1>
      <p className="home-subtitle">Discover and explore movies with ease.</p>

      {/* Buttons for Login and Register */}
      {!user && (
        <div className="button-container">
          <button className="button" onClick={() => navigate("/login")}>
            Login
          </button>
          <button className="button register-button" onClick={() => navigate("/register")}>
            Register
          </button>
        </div>
      )}

      {user && <p className="welcome-message">🎉 Welcome back, {user.name}!</p>}
    </div>
  );
};

export default Home;
