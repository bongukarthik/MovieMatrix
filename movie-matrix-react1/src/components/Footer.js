import React from "react";
import "../styles/Footer.css"; // ✅ Import the CSS file

const Footer = () => {
  return (
    <footer className="footer">
      <p>&copy; {new Date().getFullYear()} Movie Matrix. All Rights Reserved.</p>
    </footer>
  );
};

export default Footer;
