import React, { useContext } from "react";
import { Link } from "react-router-dom";
import { AuthContext } from "../context/AuthProvider";
import "../styles/Navbar.css"; // Add CSS for styling

const Navbar = () => {
    const { user, logout } = useContext(AuthContext);

    console.log("User in Navbar:", user);
    // const roleChoices = user?.role === "ADMIN" ? ["ADMIN", "USER"] : ["USER"];
    const adminRole = user?.roles.includes("ADMIN") || user?.role === "ADMIN" ? ["ADMIN", "USER"] : ["USER"];
    return (
        <nav className="navbar">
            <h2>
                <Link to="/" className="logo">MovieMatrix</Link>
            </h2>
            <ul className="nav-links">
                {user ? (
                    <>
                        {/* <li><Link to="/dashboard">Dashboard</Link></li> */}
                        {adminRole.includes("ADMIN")  ? (
                            <li><Link to="/admin">Admin Dashboard</Link></li>
                        ) : (
                            <li><Link to="/dashboard">User Dashboard</Link></li>
                        )}
                        <li><Link to="/profile">Profile</Link></li> {/* ✅ Added Profile Link */}
                        <li>
                            <button onClick={logout} className="logout-btn">Logout</button>
                        </li>
                    </>
                ) : (
                    <>
                        <li><Link to="/login">Login</Link></li>
                        <li><Link to="/register">Register</Link></li>
                    </>
                )}
            </ul>
        </nav>
    );
};

export default Navbar;
