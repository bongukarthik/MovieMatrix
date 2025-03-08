import React, { useState, useEffect, useContext } from "react";
import { AuthContext } from "../context/AuthProvider"; // Import Auth Context
import { TextField, Button, Avatar, Grid, Box, Typography } from "@mui/material";
import apiService from "../admin/service/apiService"; // Your API service to make requests

const ProfilePage = () => {
  const { user } = useContext(AuthContext); // Get logged-in user
  const [profile, setProfile] = useState({});
  const [isEditing, setIsEditing] = useState(false);
  const [updatedProfile, setUpdatedProfile] = useState({});
  const [selectedFile, setSelectedFile] = useState(null); // Handle file upload

  useEffect(() => {
    if (user) {
      // Fetch user profile data from the backend
      apiService.get(`/profile/${user.id}`)
        .then(response => {
          setProfile(response);
          setUpdatedProfile(response);
        })
        .catch(error => console.error("Error fetching profile:", error));
    }
  }, [user]);

  // Handle form field changes
  const handleChange = (e) => {
    setUpdatedProfile({ ...updatedProfile, [e.target.name]: e.target.value });
  };

  // Handle file selection
  const handleFileChange = (e) => {
    setSelectedFile(e.target.files[0]);
  };

  // Save the updated profile
  const handleSave = async () => {
    try {
      // Upload profile picture if selected
      if (selectedFile) {
        const formData = new FormData();
        formData.append("file", selectedFile);

        const uploadResponse = await apiService.post("/profile/uploadProfilePicture", formData, {
        //   headers: { "Content-Type": "multipart/form-data" }
        });

        updatedProfile.profilePicture = uploadResponse.data.fileUrl; // Save uploaded file URL
      }

      // Update profile
      await apiService.put(`/profile/update`, updatedProfile, {
        headers: { "Content-Type": "application/json" },
      });
      setProfile(updatedProfile);
      setIsEditing(false);
    } catch (error) {
      console.error("Error updating profile:", error);
    }
  };

  return (
    <Box sx={{ maxWidth: 600, margin: "auto", mt: 4, p: 2 }}>
      <Typography variant="h4" gutterBottom>Profile</Typography>
      
      <Grid container spacing={2}>
        <Grid item xs={12} sm={4}>
          <Avatar
            alt="Profile Picture"
            src={profile?.profilePicture || "/default-avatar.png"}
            sx={{ width: 120, height: 120 }}
          />
          {isEditing && (
            <input type="file" accept="image/*" onChange={handleFileChange} />
          )}
        </Grid>

        <Grid item xs={12} sm={8}>
          {isEditing ? (
            <>
              <TextField label="Name" name="name" value={updatedProfile?.name || ""} onChange={handleChange} fullWidth margin="normal" />
              <TextField label="Email" name="email" value={updatedProfile?.email || ""} onChange={handleChange} fullWidth margin="normal" />
            </>
          ) : (
            <>
              <Typography variant="h6">{profile?.name}</Typography>
              <Typography variant="body1">{profile?.email}</Typography>
            </>
          )}
          
          <Button variant="contained" onClick={() => setIsEditing(!isEditing)} sx={{ mt: 2 }}>
            {isEditing ? "Cancel" : "Edit"}
          </Button>
          
          {isEditing && (
            <Button variant="contained" color="primary" onClick={handleSave} sx={{ mt: 2, ml: 2 }}>
              Save
            </Button>
          )}
        </Grid>
      </Grid>
    </Box>
  );
};

export default ProfilePage;
