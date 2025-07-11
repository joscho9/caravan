import Header from "../components/Header.jsx";
import Footer from "../components/Footer.jsx";


const Impressum = () => {
    return (
      <div className="page-container">
        <Header />
        <div className="impressum-content">
          <h1>Impressum</h1>
          <p>Angaben gemäß § 5 TMG</p>
          <p>Max Mustermann<br />Musterstraße 1<br />12345 Musterstadt</p>
          
          <h2>Kontakt</h2>
          <p>Telefon: 01234/567890<br />E-Mail: info@example.com</p>
  
          <h2>Haftungsausschluss</h2>
          <p>Dies ist ein Beispiel-Impressum. Bitte rechtlich prüfen lassen!</p>
        </div>
        <Footer />
      </div>
    );
  };
  
  export default Impressum;