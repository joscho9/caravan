import { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import CaravanImageSection from "./CaravanImageSection.jsx";
import CaravanSpecs from "./Caravan.jsx";
import BookingForm from "./BookingForm.jsx";
import CaravanDescription from "./CaravanDescription.jsx";
import Header from "./Header.jsx";

const API_URL = import.meta.env.VITE_API_URL;


export default function CaravanDetail() {
    const { id } = useParams();
    const [caravan, setCaravan] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [bookingDetails, setBookingDetails] = useState({
        startDate: '',
        endDate: '',
        totalPrice: 0
    });

    useEffect(() => {
        fetch(`${API_URL}/caravans/${id}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error("Caravan nicht gefunden");
                }
                return response.json();
            })
            .then(data => {
                setCaravan(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error.message);
                setLoading(false);
            });
    }, [id]);

    const handleDatesSelected = (startDate, endDate, totalPrice) => {
        setBookingDetails({
            startDate,
            endDate,
            totalPrice
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        // Here you would handle the booking submission
        console.log("Booking details:", bookingDetails);

        // Example: Send booking data to your API
        // fetch('http://localhost:8080/api/bookings', {
        //     method: 'POST',
        //     headers: { 'Content-Type': 'application/json' },
        //     body: JSON.stringify({
        //         caravanId: id,
        //         startDate: bookingDetails.startDate,
        //         endDate: bookingDetails.endDate,
        //         totalPrice: bookingDetails.totalPrice
        //     })
        // })
        // .then(response => response.json())
        // .then(data => {
        //     // Handle success - maybe redirect to confirmation page
        // })
        // .catch(error => {
        //     // Handle error
        // });
    };

    if (loading) return <p>Lädt...</p>;
    if (error) return <p>Fehler: {error}</p>;
    if (!caravan) return <p>Kein Caravan gefunden</p>;

    return (
        <div className="caravan-detail">
            <Header />
            <main>
                <section id="left_main_column">
                    <article>
                        <CaravanImageSection caravan={caravan} caravanId={id} />
                        <h2>{caravan.wohnwagentyp}</h2>
                    </article>
                    <div className="hairline"></div>
                    <CaravanSpecs caravan={caravan} />
                    <div className="hairline"></div>

                    {/* Neue Ausstattungsliste */}
                    {caravan.ausstattungsmerkmale && caravan.ausstattungsmerkmale.length > 0 && (
                        <section className="features-section">
                            <h3>Die wichtigsten Ausstattungsmerkmale</h3>
                            <ul className="features-list">
                                {caravan.ausstattungsmerkmale.map((merkmal, index) => (
                                    <li key={index}>{merkmal}</li>
                                ))}
                            </ul>
                        </section>
                    )}

                    <div className="hairline"></div>
                    <CaravanDescription description={caravan.description} />
                    <div className="hairline"></div>
                    <h3>Haben Sie Fragen? Vermieter direkt kontaktieren:</h3>
                </section>

                <section id="right_col_section">
                    <BookingForm
                        caravan={caravan}
                        bookingDetails={bookingDetails}
                        onDatesSelected={handleDatesSelected}
                        onSubmit={handleSubmit}
                    />
                </section>
            </main>

            <footer>
                {/* Fußzeile mit Impressum, AGBs etc. */}
            </footer>
        </div>
    );
}