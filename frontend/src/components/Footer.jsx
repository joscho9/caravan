import React from "react";

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-container">
        <p className="footer-text">
          © {new Date().getFullYear()} Wohnwagenvermietung Uwe Scholz. Alle Rechte vorbehalten.
        </p>
        <div className="footer-links">
          <a href="/impressum">Impressum</a>
          <a href="/datenschutz">Datenschutz</a>
          <a href="/kontakt">Kontakt</a>
        </div>
      </div>
    </footer>
  );
};

export default Footer;