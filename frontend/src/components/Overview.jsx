import { useState, useEffect } from "react";
import Header from "./Header.jsx";

export default function CaravanList() {
    const [caravans, setCaravans] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch(`${process.env.REACT_APP_API_URL}/caravans/all`)
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

                return(
                <div key={caravan.id} className="caravan-tile">
                    <a href={`/caravan/${caravan.id}`}>
                        <img
                            src={`/images/${caravan.imagePath}/main.webp`}
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
