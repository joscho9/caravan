import React, { useState } from "react";
import "../index.css";

const API_URL = import.meta.env.VITE_API_URL;

// Helper function to get location display name
const getLocationDisplayName = (locationValue) => {
    const locationMap = {
        'hainburg-63512-de': 'Hainburg 63512, DE',
        'koeln-51147-de': 'Köln 51147, DE'
    };
    return locationMap[locationValue] || locationValue;
};

const BookingPopup = ({ onClose, bookingData: bookingDataProp }) => {
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

        const bookingData = {
            name: formData.name,
            email: formData.email,
            subject: formData.subject,
            message: formData.message,
            caravanName: bookingDataProp.caravan.name,
            caravanId: bookingDataProp.caravan.id,
            startDate: bookingDataProp.startDate,
            endDate: bookingDataProp.endDate,
            location: bookingDataProp.location,
            totalPrice: bookingDataProp.totalPrice,
            pricePerDay: bookingDataProp.totalPrice / ((new Date(bookingDataProp.endDate) - new Date(bookingDataProp.startDate)) / (1000 * 60 * 60 * 24) + 1)
        };

        try {
            const response = await fetch(`${API_URL}/bookings`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(bookingData)
            });

            if (response.ok) {
                setSubmitStatus('success');
                setFormData({ name: '', email: '', subject: '', message: '' });
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
            <div className="popup-content" style={{ position: 'relative' }}>
                <button className="close-x" onClick={onClose} aria-label="Schließen">×</button>
                <h2>Buchungsanfrage</h2>
                
                {bookingDataProp && (
                    <div className="booking-summary">
                        <p><strong>Wohnwagen:</strong> {bookingDataProp.caravan?.name}</p>
                        <p><strong>Startdatum:</strong> {bookingDataProp.startDate}</p>
                        <p><strong>Enddatum:</strong> {bookingDataProp.endDate}</p>
                        <p><strong>Standort:</strong> {getLocationDisplayName(bookingDataProp.location)}</p>
                        <p><strong>Gesamtpreis:</strong> {bookingDataProp.totalPrice ? bookingDataProp.totalPrice.toFixed(2) + '€' : 'Nicht berechnet'}</p>
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
                        <button type="button" className="cancel-btn" onClick={onClose}>
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
