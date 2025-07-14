import React, { useState } from "react";
import "./BookingPopup.css";

const API_URL = import.meta.env.VITE_API_URL;

const BookingPopup = ({ onClose, bookingData }) => {
    const [formData, setFormData] = useState({
        name: '',
        email: '',
        subject: '',
        message: ''
    });
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [submitStatus, setSubmitStatus] = useState(null);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setIsSubmitting(true);
        setSubmitStatus(null);

        // Erstelle eine erweiterte Nachricht mit Buchungsdetails
        const enhancedMessage = `Buchungsanfrage für ${bookingData?.caravan?.name || 'Wohnwagen'}:

${formData.message}

--- Buchungsdetails ---
Startdatum: ${bookingData?.startDate || 'Nicht ausgewählt'}
Enddatum: ${bookingData?.endDate || 'Nicht ausgewählt'}
Standort: ${bookingData?.location || 'Hainburg 63512, DE'}
Gesamtpreis: ${bookingData?.totalPrice ? bookingData.totalPrice.toFixed(2) + '€' : 'Nicht berechnet'}
Preis pro Tag: ${bookingData?.caravan?.price_per_day ? bookingData.caravan.price_per_day.toFixed(2) + '€' : 'Nicht verfügbar'}`;

        const contactData = {
            ...formData,
            subject: `Buchungsanfrage: ${bookingData?.caravan?.name || 'Wohnwagen'} - ${formData.subject}`,
            message: enhancedMessage
        };

        try {
            const response = await fetch(`${API_URL}/contact`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(contactData)
            });

            if (response.ok) {
                setSubmitStatus('success');
                setFormData({ name: '', email: '', subject: '', message: '' });
                // Schließe Popup nach 3 Sekunden
                setTimeout(() => {
                    onClose();
                }, 3000);
            } else {
                setSubmitStatus('error');
            }
        } catch (error) {
            console.error('Error submitting booking form:', error);
            setSubmitStatus('error');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="popup-overlay">
            <div className="popup-content">
                <h2>Buchungsanfrage</h2>
                
                {bookingData && (
                    <div className="booking-summary">
                        <h3>Ihre Auswahl:</h3>
                        <p><strong>Wohnwagen:</strong> {bookingData.caravan?.name}</p>
                        <p><strong>Startdatum:</strong> {bookingData.startDate}</p>
                        <p><strong>Enddatum:</strong> {bookingData.endDate}</p>
                        <p><strong>Gesamtpreis:</strong> {bookingData.totalPrice ? bookingData.totalPrice.toFixed(2) + '€' : 'Nicht berechnet'}</p>
                    </div>
                )}

                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label htmlFor="name">Name *</label>
                        <input
                            type="text"
                            id="name"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="email">E-Mail *</label>
                        <input
                            type="email"
                            id="email"
                            name="email"
                            value={formData.email}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="subject">Betreff *</label>
                        <input
                            type="text"
                            id="subject"
                            name="subject"
                            value={formData.subject}
                            onChange={handleChange}
                            placeholder="z.B. Buchungsanfrage für Wohnwagen"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="message">Nachricht *</label>
                        <textarea
                            id="message"
                            name="message"
                            value={formData.message}
                            onChange={handleChange}
                            required
                            rows="4"
                            placeholder="Ihre Nachricht an uns..."
                        />
                    </div>

                    <div className="popup-buttons">
                        <button 
                            type="submit" 
                            className="submit-btn"
                            disabled={isSubmitting}
                        >
                            {isSubmitting ? 'Wird gesendet...' : 'Buchungsanfrage senden'}
                        </button>
                        <button type="button" onClick={onClose}>
                            Abbrechen
                        </button>
                    </div>

                    {submitStatus === 'success' && (
                        <div className="success-message">
                            ✅ Ihre Buchungsanfrage wurde erfolgreich gesendet! Wir melden uns zeitnah bei Ihnen.
                        </div>
                    )}

                    {submitStatus === 'error' && (
                        <div className="error-message">
                            ❌ Es gab einen Fehler beim Senden Ihrer Buchungsanfrage. Bitte versuchen Sie es erneut.
                        </div>
                    )}
                </form>
            </div>
        </div>
    );
};

export default BookingPopup;
