import VanillaCalendar from "./Calendar.tsx";
import BookingPopup from "./BookingPopup.jsx";
import {useState, useEffect} from "react";

export default function BookingForm({ caravan, bookingDetails, onDatesSelected, onSubmit }) {
    const [showPopup, setShowPopup] = useState(false);
    const [startDate, setStartDate] = useState('');
    const [endDate, setEndDate] = useState('');
    const [location, setLocation] = useState('hainburg-63512-de');

    // Format date helper function
    const formatDate = (date) => {
        if (!date) return '';
        const d = new Date(date);
        return d.toISOString().split('T')[0];
    };

    // Handle date selection from calendar
    const handleOnDateClick = (calendarInstance) => {

        console.log(calendarInstance.context.selectedDates);

        try {
            // Access selected dates based on the calendar API
            const selectedDates = calendarInstance.context.selectedDates;
            console.log("Selected dates Barilla:", selectedDates);


            if (selectedDates && selectedDates.length > 1) {

                // Sort dates to ensure correct order
                const sortedDates = [...selectedDates].sort((a, b) => new Date(a) - new Date(b));

                // Update start and end dates with proper formatting
                const formattedStartDate = formatDate(sortedDates[0]);
                const formattedEndDate = formatDate(sortedDates[sortedDates.length - 1]);

                console.log("Setting dates:", formattedStartDate, formattedEndDate);

                setStartDate(formattedStartDate);
                setEndDate(formattedEndDate);

                // Calculate total price based on seasonal pricing
                const startDateObj = new Date(sortedDates[0]);
                const endDateObj = new Date(sortedDates[sortedDates.length - 1]);
                
                let totalPrice = 0;
                let currentDate = new Date(startDateObj);
                
                // Calculate price for each day based on season
                while (currentDate <= endDateObj) {
                    const month = currentDate.getMonth() + 1; // getMonth() returns 0-11
                    const priceForDay = (month >= 4 && month <= 8) ? caravan.summerPrice : caravan.winterPrice;
                    totalPrice += priceForDay;
                    currentDate.setDate(currentDate.getDate() + 1);
                }
                
                const timeDifference = endDateObj.getTime() - startDateObj.getTime();
                const numberOfDays = Math.ceil(timeDifference / (1000 * 3600 * 24)) + 1;
                
                console.log("Price calculation debug:", {
                    selectedDatesArray: sortedDates,
                    selectedDatesCount: sortedDates.length,
                    startDate: sortedDates[0],
                    endDate: sortedDates[sortedDates.length - 1],
                    calculatedDays: numberOfDays,
                    summerPrice: caravan.summerPrice,
                    winterPrice: caravan.winterPrice,
                    totalPrice: totalPrice
                });

                // Call parent callback if needed
                if (onDatesSelected) {
                    onDatesSelected(formattedStartDate, formattedEndDate, totalPrice);
                }
            }
            if(selectedDates && selectedDates.length == 1){
                setStartDate(formatDate(selectedDates[0]));
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
                    <input type="date" id="startdatum" name="startdatum" readOnly value={startDate || ''}/>
                    <label htmlFor="enddatum">Enddatum</label>
                    <input type="date" id="enddatum" name="enddatum" readOnly value={endDate || ''}/>
                </div>

                <div className="calendar-wrapper">
                    <h4>Verfügbare Tage auswählen:</h4>
                    <VanillaCalendar
                        summerPrice={caravan.summerPrice}
                        winterPrice={caravan.winterPrice}
                        config={{
                            onClickDate: (self, event) => {
                                handleOnDateClick(self);
                            },
                            locale: 'de-DE',
                            selectionDatesMode: 'multiple-ranged',
                            dateMin: new Date(),
                            dateMax: new Date(new Date().setFullYear(new Date().getFullYear() + 1)),
                            disableDates: caravan.unavailableDates || [],
                            disableDatesGaps: true,
                        }}
                    />
                </div>

                <div>
                    <label htmlFor="standort">Standort</label>
                    <select id="standort" name="standort" value={location} onChange={e => setLocation(e.target.value)}>
                        <option value="hainburg-63512-de">Hainburg 63512, DE (Lieferung)</option>
                        <option value="koeln-51147-de">Köln 51147, DE (Lieferung)</option>
                    </select>
                </div>

                <div className="price-summary">
                    <p>Sommer (Apr-Aug): <span>{caravan.summerPrice?.toFixed(2)}€</span></p>
                    <p>Winter (Sep-Mär): <span>{caravan.winterPrice?.toFixed(2)}€</span></p>
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
            {showPopup && (
                <BookingPopup 
                    onClose={() => setShowPopup(false)} 
                    bookingData={{
                        caravan: caravan,
                        startDate: startDate,
                        endDate: endDate,
                        totalPrice: bookingDetails?.totalPrice,
                        location: location
                    }}
                />
            )}
        </div>
    );
}