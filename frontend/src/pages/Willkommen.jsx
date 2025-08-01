import { Link } from "react-router-dom";
import Header from "../components/Header.jsx";
import Footer from "../components/Footer.jsx";

export default function Willkommen() {
    return (
        <div id="root">
            <Header />
            <main className="willkommen-container">
                <div className="willkommen-content">
                    <h2>Willkommen bei unserer Wohnwagenvermietung</h2>
                    
                    <p>
                        Herzlich willkommen bei unserer seit 2012 bestehenden Wohnwagenvermietung 
                        in Niederkassel bei Köln und in Hainburg bei Seligenstadt.
                    </p>
                    
                    <p>
                        Wenn Sie einen vollausgestatteten, vollkaskoversicherten Wohnwagen oder ein 
                        Dachzelt für Ihren Urlaub suchen und Wert auf einen bereits sehr erfahrenen 
                        Vermieter legen sind Sie bei uns genau richtig.
                    </p>
                    
                    <h3>Unser Motto für Ihren Urlaub:</h3>
                    
                    <p>
                        Persönliche Kleidung und Bettzeug in den Wohnwagen einräumen, Kühlschrank 
                        befüllen, ankuppeln und schon können Sie in ihren Urlaub starten.
                    </p>
                    
                    <p>
                        Schauen Sie sich unsere drei Wohnwagen und das Dachzelt näher an und entscheiden 
                        Sie, welches Model am besten zu Ihnen passt. Die noch buchbaren Termine stehen 
                        direkt neben den jeweiligen Beschreibungen der Wohnwagen.
                    </p>
                    
                    <p>
                        Die Übergabeorte unserer drei eigenen Wohnwagen und des Dachzeltes mit jeweils 
                        einer individuellen persönlichen und umfangreichen Einweisung sind entweder in 
                        Niederkassel bei Köln, in Hainburg bei Seligenstadt oder individuell mit Ihnen 
                        abgestimmt auch im Bereich der A3 zwischen Köln und Aschaffenburg.
                    </p>
                    
                    <p>
                        Wir freuen uns auf Ihre Anfrage - werden auch Sie ein weiterer zufriedener Kunde!
                    </p>
                    
                    <p className="signature">
                        <strong>Ihre Familie Scholz</strong>
                    </p>
                    
                    <div className="back-to-home">
                        <Link to="/" className="back-button">Zurück zur Startseite</Link>
                    </div>
                </div>
            </main>
            <Footer />
        </div>
    );
}