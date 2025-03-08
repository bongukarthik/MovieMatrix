import React, { useContext } from "react";
import { AuthContext } from "../context/AuthProvider";

const Dashboard = () => {
    const { user } = useContext(AuthContext);
    return <h2>Welcome to the Dashboard, {user ? user.name : "Guest"}!</h2>;
};

export default Dashboard;
