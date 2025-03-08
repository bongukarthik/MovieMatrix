import React from "react";
import { Layout } from "react-admin";
import AdminAppBar from "./AdminAppBar"; // Import custom AppBar

const AdminLayout = (props) => <Layout {...props} appBar={AdminAppBar} />;

export default AdminLayout;
