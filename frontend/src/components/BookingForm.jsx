import VanillaCalendar from "./Calendar.tsx";
import BookingPopup from "./BookingPopup.jsx";
import {useState, useEffect} from "react";

export default function BookingForm({ caravan, bookingDetails, onDatesSelected, onSubmit }) {
    const [showPopup, setShowPopup] = useState(false);
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');

    // Format date helper function
    const formatDate = (date) => {
        if (!date) return '';
        const d = new Date(date);
        return d.toISOString().split('T')[0];
    };

    // Handle date selection from calendar
    const handleDatesSelected = (calendarInstance) => {

        console.log(calendarInstance.context.selectedDates);

        try {
            // Access selected dates based on the calendar API
            const selectedDates = calendarInstance.context.selectedDates;

            console.log("Selected dates:", selectedDates);


            if (selectedDates && selectedDates.length > 1) {
                // Sort dates to ensure correct order
                const sortedDates = [...selectedDates].sort((a, b) => new Date(a) - new Date(b));

                // Update start and end dates with proper formatting
                const formattedStartDate = formatDate(sortedDates[0]);
                const formattedEndDate = formatDate(sortedDates[sortedDates.length - 1]);

                console.log("Setting dates:", formattedStartDate, formattedEndDate);

                setStartDate(formattedStartDate);
                setEndDate(formattedEndDate);

                // Call parent callback if needed
                if (onDatesSelected) {
                    onDatesSelected({
                        startDate: sortedDates[0],
                        endDate: sortedDates[sortedDates.length - 1],
                        selectedDates: sortedDates
                    });
                }
            }
        } catch (error) {
            console.error("Error processing selected dates:", error);
        }
    };

    return (
        <div className="glowing-box">
            <form onSubmit={onSubmit}>
                <div>
                    <label htmlFor="startdatum">Startdatum</label>
                    <input
                        type="date"
                        id="startdatum"
                        name="startdatum"
                        readOnly
                        value={startDate || ''}
                    />

                    <label htmlFor="enddatum">Enddatum</label>
                    <input
                        type="date"
                        id="enddatum"
                        name="enddatum"
                        readOnly
                        value={endDate || ''}
                    />
                </div>

                <div className="calendar-wrapper">
                    <h4>Verfügbare Tage auswählen:</h4>
                    <VanillaCalendar
                        pricePerDay={caravan.price_per_day || 0}
                        config={{
                            onClickDate: (self, event) => {
                                handleDatesSelected(self);
                            },
                            locale: 'de-AT',
                            selectionDatesMode: 'multiple-ranged',
                            dateMin: new Date(),
                            dateMax: new Date(new Date().setFullYear(new Date().getFullYear() + 1)),
                            disableDates: caravan.unavailableDates || [],
                        }}
                    />
                </div>

                <div>
                    <label htmlFor="standort">Standort</label>
                    <select id="standort" name="standort">
                        <option value="hainburg-63512-de">Hainburg 63512, DE (Lieferung)</option>
                    </select>
                </div>

                <div className="price-summary">
                    <p>Standard-Tag: <span>{caravan.price_per_day?.toFixed(2)}€</span></p>
                    <p className="font-semibold">Gesamtsumme: <span id="total-price">
                        {bookingDetails?.totalPrice ? bookingDetails.totalPrice.toFixed(2) + "€" : "(Datum auswählen)"}
                    </span></p>
                </div>

                <div className="booking-button">
                    <button
                        type="button"
                        disabled={!startDate || !endDate}
                        onClick={() => {
                            console.log("Booking details:", {startDate, endDate, bookingDetails});
                            setShowPopup(true);
                        }}
                    >
                        Jetzt anfragen
                    </button>
                </div>
            </form>
            {showPopup && <BookingPopup onClose={() => setShowPopup(false)} />}
        </div>
    );
}