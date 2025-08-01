import { Link } from "react-router-dom";
import "./Header.css";

export default function Header() {
    return (
        <header className="header">
            <div className="header-overlay">
                <Link to="/" className="header-link">Zurück zur Übersicht</Link>
                <h1 className="header-title">Wohnwagenvermietung</h1>
                <p className="header-subtitle">Dein Abenteuer beginnt hier.</p>
                <Link to="/willkommen" className="welcome-button">Mehr über uns erfahren</Link>
            </div>
        </header>
    );
}
