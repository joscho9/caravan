// Importiere die React-Hooks useEffect, useRef und useState aus dem React-Paket
import { useEffect, useRef, useState } from 'react';
// Importiere die Typen Options und die Klasse Calendar aus der Bibliothek vanilla-calendar-pro
import { Options, Calendar } from 'vanilla-calendar-pro';

// Importiere die CSS-Stile der vanilla-calendar-pro Bibliothek, um das Standardstyling anzuwenden
import 'vanilla-calendar-pro/styles/index.css';

// Definiert das Interface CalendarProps, welches von den Standard-HTML-Attributen für ein div-Element erbt
interface CalendarProps extends React.HTMLAttributes<HTMLDivElement> {
    // Optionales config-Attribut vom Typ Options, um Kalenderkonfigurationen zu übergeben
    config?: Options,
}

// Definiert und exportiert die Standardfunktion VanillaCalendar als React-Komponente
export default function VanillaCalendar({ config, ...attributes }: CalendarProps) {
    // Erzeugt eine Referenz (ref) für das DOM-Element, in dem der Kalender initialisiert wird
    const ref = useRef(null);
    // Deklariert einen state-Hook "calendar" für die Instanz des Kalenders, initialisiert mit null
    const [calendar, setCalendar] = useState<Calendar | null>(null);

    // useEffect-Hook, der ausgeführt wird, wenn sich die Referenz oder die config ändert
    useEffect(() => {
        // Prüft, ob die Referenz (ref.current) existiert, bevor fortgefahren wird
        if (!ref.current) return;
        // Erzeugt eine neue Instanz des Kalenders mit dem DOM-Element und der übergebenen Konfiguration und speichert sie im state
        setCalendar(new Calendar(ref.current, config));
    }, [ref, config]) // Der Effekt wird erneut ausgeführt, wenn sich ref oder config ändert

    // Ein weiterer useEffect-Hook, der ausgeführt wird, wenn sich die Kalenderinstanz ändert
    useEffect(() => {
        // Wenn keine Kalenderinstanz vorhanden ist, wird der Effekt abgebrochen
        if (!calendar) return;
        // Initialisiert den Kalender, indem die init()-Methode der Kalenderinstanz aufgerufen wird
        calendar.init()
    }, [calendar]) // Dieser Effekt wird erneut ausgeführt, sobald sich die Kalenderinstanz ändert

    // Rendern des div-Elements, welches alle zusätzlichen HTML-Attribute erhält und an das ref gebunden wird
    return (
        <div {...attributes} ref={ref}></div>
    )
}
