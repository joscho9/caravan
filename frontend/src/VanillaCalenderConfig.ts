// Imports
// @ts-ignore
import VanillaCalendar from 'vanilla-calendar-pro';
import {Options} from 'vanilla-calendar-pro/types';
import 'vanilla-calendar-pro/build/vanilla-calendar.min.css';
// @ts-ignore
import moment from "moment";



// Definiere eine Klasse für das Kalendermodul
export class CalendarModule {
    private startInput: HTMLInputElement;
    private endInput: HTMLInputElement;
    private calendarContainer: HTMLElement;
    private calendar: VanillaCalendar;
    private totalPriceSpan: HTMLElement;

    constructor(startInputId: string, endInputId: string, calendarContainerId: string) {
        this.startInput = document.getElementById(startInputId) as HTMLInputElement;
        this.endInput = document.getElementById(endInputId) as HTMLInputElement;
        this.calendarContainer = document.getElementById(calendarContainerId) as HTMLElement;
        this.totalPriceSpan = document.getElementById('total-price') as HTMLElement;

        const options: Options = this.getOptions();
        this.calendar = new VanillaCalendar('#calendar', options);
        this.addEventListeners();
    }

    private calculatePrice() {
        let total: number = 0;
        let caravanPrices = JSON.parse(this.calendarContainer.getAttribute('data-geld'));
        const selectedDates = this.calendar.selectedDates.map(date => new Date(date));
        for (const selectedDate of selectedDates) {
            total += Number(caravanPrices[moment(selectedDate).dayOfYear()]);
        }
        this.totalPriceSpan.innerHTML = total + "€";

    }

    private getOptions(): Options {
        return {
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
            CSSClasses: {
                month: "month-custom",
                year: "year-custom",
            },

            actions: {
                clickDay: (e, self) => this.handleDayClick(e),
                getDays(day, date, HTMLElement, HTMLButtonElement, self) {
                    const cal = document.getElementById("mein_kalender") as HTMLElement;
                    // console.log(date)

                    let [y,m, d] = date.split("-");
                    let caravanPrices = JSON.parse(cal.getAttribute('data-geld'));

                    let calenderDate = new Date(2000, Number(m)-1, Number(d));

                    let price = caravanPrices[moment(calenderDate).dayOfYear()];
                    price = Math.round(price);

                    HTMLButtonElement.style.flexDirection = 'column';
                    HTMLButtonElement.innerHTML = `
                            <span>${day}</span>
                            <span style="font-size: 9px;color: #8BC34A;">${price}€</span>
                            `;
                },
            },
        };
    }

    private handleDayClick(e: Event) {

        e.stopPropagation();
        //console.log("Clicked a day");
        const selectedDates = this.calendar.selectedDates.map(date => new Date(date));

        if (selectedDates.length > 0) {
            this.startInput.value = selectedDates[0].toISOString().split('T')[0];
        }

        if (selectedDates.length > 1) {
            this.endInput.value = selectedDates[selectedDates.length - 1].toISOString().split('T')[0];
            //this.calendarContainer.style.display = 'none';
            this.calculatePrice();
        }
    }


    private addEventListeners() {
        this.startInput.addEventListener('click', () => {
            this.calendarContainer.style.display = 'block';
        });

        this.endInput.addEventListener('click', () => {
            this.calendarContainer.style.display = 'block';
        });


        document.addEventListener('click', (e) => {

            console.dir(e.target as Node);
            console.log(this.calendarContainer.contains(e.target as Node));

            if (!this.calendarContainer.contains(e.target as Node) && e.target !== this.startInput && e.target !== this.endInput) {
                this.calendarContainer.style.display = 'none';
            }
        });

    }

    public init() {
        this.calendar.init();
    }
}
