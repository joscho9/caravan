import { useState, useEffect } from "react";
import Header from "./Header.jsx";

const API_URL = import.meta.env.VITE_API_URL || "http://localhost:8080/api";


export default function CaravanList() {
    const [caravans, setCaravans] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch(`${API_URL}/caravans/all`)
            .then(response => response.json())
            .then(data => {
                setCaravans(data);
                setLoading(false);
            })
            .catch(error => {
                console.error("Fehler beim Laden der Wohnmobile:", error);
                setLoading(false);
            });
    }, []);

    if (loading) return <p>Loading...</p>;

    return (
        <>
        <Header />
        <section className="overview-container">
            {caravans.map(caravan => {

                // Debugging: file_path in Konsole ausgeben
                console.log("Bildpfad:", caravan.imagePath);
                //console.log(caravan.images[0].imagePath);

                return(
                <div key={caravan.id} className="caravan-tile">
                    <a href={`/caravan/${caravan.id}`}>
                        <img
                            // todo remove src={`${API_URL}/uploads/${caravan.images && caravan.images[0] && caravan.images[0].imagePath}`}
                            src={`${API_URL}/uploads/${caravan.id}/${caravan.mainImagePath}`}
                            alt={caravan.wohnwagentyp}
                        />
                        <div className="caravan-info">
                            <h3>{caravan.wohnwagentyp}</h3>
                        </div>
                    </a>
                </div>
            )})}
        </section>
        </>
    );
}
