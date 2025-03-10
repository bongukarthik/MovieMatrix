import React, { useState, useEffect, useContext } from "react";
import { AuthContext } from "../context/AuthProvider"; // Import Auth Context
import { TextField, Button, Avatar, Grid, Box, Typography } from "@mui/material";
import apiService from "../admin/service/apiService"; // Your API service to make requests

const ProfilePage = () => {
  const { user, token } = useContext(AuthContext); // Get logged-in user and token
  const [profile, setProfile] = useState({});
  const [isEditing, setIsEditing] = useState(false);
  const [updatedProfile, setUpdatedProfile] = useState({});
  const [selectedFile, setSelectedFile] = useState(null); // Handle file upload
  const [isChangingPassword, setIsChangingPassword] = useState(false);
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [passwordMessage, setPasswordMessage] = useState("");

  useEffect(() => {
    if (user) {
      // Fetch user profile data from the backend
      apiService.get(`/user/${user.id}`)
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

        const uploadResponse = await apiService.post("/user/uploadProfilePicture", formData);

        updatedProfile.profilePicture = uploadResponse.data.fileUrl; // Save uploaded file URL
      }

      // Update profile
      await apiService.put(`/user/update`, updatedProfile, {
        headers: { "Content-Type": "application/json" },
      });

      setProfile(updatedProfile);
      setIsEditing(false);
    } catch (error) {
      console.error("Error updating profile:", error);
    }
  };

  // Handle password change
  const handleChangePassword = async () => {
    try {
      const response = await apiService.post(
        "/user/changePassword",
        { oldPassword, newPassword },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setPasswordMessage(response.data);
      setOldPassword("");
      setNewPassword("");
      setIsChangingPassword(false);
    } catch (error) {
      setPasswordMessage("Error changing password");
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
          {isEditing && <input type="file" accept="image/*" onChange={handleFileChange} />}
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

          <Box sx={{ mt: 2 }}>
            <Button variant="contained" onClick={() => setIsEditing(!isEditing)}>
              {isEditing ? "Cancel" : "Edit"}
            </Button>

            {isEditing && (
              <Button variant="contained" color="primary" onClick={handleSave} sx={{ ml: 2 }}>
                Save
              </Button>
            )}

            <Button
              variant="contained"
              color="secondary"
              onClick={() => setIsChangingPassword(!isChangingPassword)}
              sx={{ ml: 2 }}
            >
              Change Password
            </Button>
          </Box>
        </Grid>
      </Grid>

      {/* Change Password Section */}
      {isChangingPassword && (
        <Box sx={{ mt: 4 }}>
          <Typography variant="h5">Change Password</Typography>
          <TextField
            label="Old Password"
            type="password"
            value={oldPassword}
            onChange={(e) => setOldPassword(e.target.value)}
            fullWidth
            margin="normal"
          />
          <TextField
            label="New Password"
            type="password"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            fullWidth
            margin="normal"
          />
          <Button variant="contained" color="primary" onClick={handleChangePassword} sx={{ mt: 2 }}>
            Save New Password
          </Button>
          <Button variant="outlined" onClick={() => setIsChangingPassword(false)} sx={{ mt: 2, ml: 2 }}>
            Cancel
          </Button>

          {passwordMessage && <Typography sx={{ mt: 2, color: "red" }}>{passwordMessage}</Typography>}
        </Box>
      )}
    </Box>
  );
};

export default ProfilePage;
