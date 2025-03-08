import React from "react";
import { Menu } from "react-admin";
import MovieIcon from "@mui/icons-material/Movie";
import DashboardIcon from "@mui/icons-material/Dashboard";

const CustomMenu = (props) => (
    <Menu {...props}>
        {/* Admin Dashboard Link */}
        <Menu.Item to="/" primaryText="Dashboard" leftIcon={<DashboardIcon />} />

        {/* Movies Section */}
        <Menu.Item to="/admin/movies" primaryText=" All Movies" leftIcon={<MovieIcon />} />
    </Menu>
);

export default CustomMenu;
