import React, { useState } from "react";
import "./BookingPopup.css"; // Styles separat

const BookingPopup = ({ onClose }) => {
    const [name, setName] = useState("");
    const [message, setMessage] = useState("");


    const handleSubmit = async (e) => {
        e.preventDefault();

        const payload = {
            name,
            message,
        };

        try {
            const response = await fetch("/api/buchung", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            });

            if (response.ok) {
                alert("Buchung erfolgreich!");
                onClose(); // Fenster schließen
            } else {
                alert("Fehler beim Absenden.");
            }
        } catch (error) {
            alert("Netzwerkfehler.");
            console.error(error);
        }
    };

    return (
        <div className="popup-overlay">
            <div className="popup-content">
                <h2>Buchungsanfrage</h2>
                <form onSubmit={handleSubmit}>
                    <label>
                        Name:
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            required
                        />
                    </label>
                    <label>
                        Nachricht:
                        <textarea
                            value={message}
                            onChange={(e) => setMessage(e.target.value)}
                            required
                        />
                    </label>
                    <div className="popup-buttons">
                        <button type="submit">Absenden</button>
                        <button type="button" onClick={onClose}>
                            Abbrechen
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default BookingPopup;
