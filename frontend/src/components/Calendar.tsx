import { useEffect, useRef, useState } from 'react';
import { Options, Calendar } from 'vanilla-calendar-pro';

import 'vanilla-calendar-pro/styles/index.css';

interface CalendarProps extends React.HTMLAttributes<HTMLDivElement> {
    // Optionales config-Attribut vom Typ Options, um Kalenderkonfigurationen zu übergeben
    config?: Options,
}

export default function VanillaCalendar({ config, ...attributes }: CalendarProps) {
    const ref = useRef(null);
    const [calendar, setCalendar] = useState<Calendar | null>(null);

    useEffect(() => {
        if (!ref.current) return;
        setCalendar(new Calendar(ref.current, config));
        console.log("hooki hook");
        
    }, []) 

    useEffect(() => {
        if (!calendar) return;
        calendar.init()
        console.log("Pro Arbeit");

    }, [calendar]) 

    return (
        <div {...attributes} ref={ref}></div>
    )
}
