import React from "react";
import { AppBar } from "react-admin";
import { Typography, Box, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";

const AdminAppBar = (props) => {
  const navigate = useNavigate();

  return (
    <AppBar {...props}>
      <Box sx={{ display: "flex", alignItems: "center", width: "100%", justifyContent: "space-between", padding: "0 16px" }}>
        {/* Left Side: Admin Title */}
        <Typography variant="h6" sx={{ flexGrow: 1 }}>
          Admin Dashboard
        </Typography>

        {/* Right Side: Custom Navigation Buttons */}
        <Box>
          <Button color="inherit" onClick={() => navigate("/")}>
            Home
          </Button>
          <Button color="inherit" onClick={() => navigate("/movies")}>
            Movies
          </Button>
          <Button color="inherit" onClick={() => navigate("/logout")}>
            Logout
          </Button>
        </Box>
      </Box>
    </AppBar>
  );
};

export default AdminAppBar;
