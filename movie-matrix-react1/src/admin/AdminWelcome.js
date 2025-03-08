import React from "react";
import { Card, CardContent, Typography } from "@mui/material";

const AdminWelcome = () => {
  return (
    <Card>
      <CardContent>
        <Typography variant="h4" gutterBottom>
          Welcome to the Admin Dashboard!
        </Typography>
        <Typography variant="body1">
          Manage movies, edit content, and keep everything up to date.
        </Typography>
      </CardContent>
    </Card>
  );
};

export default AdminWelcome;
