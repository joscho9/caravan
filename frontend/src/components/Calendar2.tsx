import React, { useEffect, useRef, useState } from 'react';
import { Calendar } from 'vanilla-calendar-pro';
import 'vanilla-calendar-pro/styles/index.css';
import moment from 'moment';

interface CalendarProps {
    startInputId: string;
    endInputId: string;
    calendarContainerId: string;
    prices: Record<string, number>; // Day of year -> price mapping
    onDatesSelected?: (startDate: string, endDate: string, totalPrice: number) => void;
}

const CalendarModule: React.FC<CalendarProps> = ({
                                                     startInputId,
                                                     endInputId,
                                                     calendarContainerId,
                                                     prices,
                                                     onDatesSelected
                                                 }) => {
    const calendarRef = useRef<HTMLDivElement>(null);
    const [calendar, setCalendar] = useState<any>(null);
    const [isVisible, setIsVisible] = useState(false);
    const [totalPrice, setTotalPrice] = useState(0);

    useEffect(() => {
        // Initialize calendar when component mounts
        if (!calendarRef.current) return;

        const options = {
            input: false,
            type: 'multiple',
            months: 2,
            jumpMonths: 2,
            settings: {
                range: {
                    disablePast: true,
                },
                selection: {
                    day: 'multiple-ranged',
                },
                visibility: {
                    daysOutside: false,
                    theme: 'light',
                },
            },
            classNames: {
                month: "month-custom",
                year: "year-custom",
            },
            actions: {
                clickDay(e: Event, calendar: any) {
                    e.stopPropagation();

                    const selectedDates = calendar.selectedDates.sort();
                    const startInput = document.getElementById(startInputId) as HTMLInputElement;
                    const endInput = document.getElementById(endInputId) as HTMLInputElement;
                    const totalPriceSpan = document.getElementById('total-price');

                    if (selectedDates.length > 0) {
                        startInput.value = new Date(selectedDates[0]).toISOString().split('T')[0];
                    }

                    if (selectedDates.length > 1) {
                        endInput.value = new Date(selectedDates[selectedDates.length - 1]).toISOString().split('T')[0];

                        // Calculate price
                        let total = 0;
                        for (const dateStr of selectedDates) {
                            const selectedDate = new Date(dateStr);
                            const dayOfYear = moment(selectedDate).dayOfYear();
                            total += Number(prices[dayOfYear] || 0);
                        }

                        setTotalPrice(total);

                        if (totalPriceSpan) {
                            totalPriceSpan.innerHTML = total + "€";
                        }

                        if (onDatesSelected) {
                            onDatesSelected(
                                new Date(selectedDates[0]).toISOString().split('T')[0],
                                new Date(selectedDates[selectedDates.length - 1]).toISOString().split('T')[0],
                                total
                            );
                        }
                    }
                },
                getDays(day: number, date: Date, HTMLElement: HTMLElement, HTMLButtonElement: HTMLButtonElement, calendar: any) {
                    const dayOfYear = moment(date).dayOfYear();
                    const price = prices[dayOfYear] || 0;

                    HTMLButtonElement.style.flexDirection = 'column';
                    HTMLButtonElement.innerHTML = `
            <span>${day}</span>
            <span style="font-size: 9px;color: #8BC34A;">${Math.round(price)}€</span>
          `;
                },
            },
        };

        const calendarInstance = new Calendar(calendarRef.current, options);
        calendarInstance.init();
        setCalendar(calendarInstance);

        // Set up event listeners for showing/hiding calendar
        const startInput = document.getElementById(startInputId);
        const endInput = document.getElementById(endInputId);

        const showCalendar = () => setIsVisible(true);

        if (startInput) startInput.addEventListener('click', showCalendar);
        if (endInput) endInput.addEventListener('click', showCalendar);

        const handleClickOutside = (e: MouseEvent) => {
            if (
                calendarRef.current &&
                !calendarRef.current.contains(e.target as Node) &&
                e.target !== startInput &&
                e.target !== endInput
            ) {
                setIsVisible(false);
            }
        };

        document.addEventListener('click', handleClickOutside);

        // Cleanup event listeners on component unmount
        return () => {
            if (startInput) startInput.removeEventListener('click', showCalendar);
            if (endInput) endInput.removeEventListener('click', showCalendar);
            document.removeEventListener('click', handleClickOutside);
            if (calendar) calendar.destroy();
        };
    }, [startInputId, endInputId, calendarContainerId, prices, onDatesSelected]);

    return (
        <div
            id={calendarContainerId}
            ref={calendarRef}
            style={{
                display: isVisible ? 'block' : 'none',
                width: '100%',
                maxWidth: '600px',
                margin: '0 auto',
                border: '1px solid #e0e0e0',
                borderRadius: '8px',
                padding: '10px',
                backgroundColor: '#fff',
                boxShadow: '0 4px 12px rgba(0,0,0,0.1)'
            }}
        />
    );
};

export default CalendarModule;