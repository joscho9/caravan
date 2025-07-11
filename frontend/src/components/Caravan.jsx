export default function CaravanSpecs({ caravan }) {
    return (
        <section>
            <h3>Technische Daten</h3>
            <table>
                <tbody>
                <tr>
                    <td>Wohnwagentyp</td>
                    <td>{caravan.wohnwagentyp || "Nicht verfügbar"}</td>
                </tr>
                <tr>
                    <td>Gesamtlänge</td>
                    <td>{caravan.gesamtlaenge || "Nicht verfügbar"}</td>
                </tr>
                <tr>
                    <td>Nutzlänge</td>
                    <td>{caravan.nutzlaenge || "Nicht verfügbar"}</td>
                </tr>
                <tr>
                    <td>Gesamtbreite</td>
                    <td>{caravan.gesamtbreite || "Nicht verfügbar"}</td>
                </tr>
                <tr>
                    <td>Leergewicht</td>
                    <td>{caravan.leergewicht || "Nicht verfügbar"}</td>
                </tr>
                <tr>
                    <td>Zulässiges Gesamtgewicht</td>
                    <td>{caravan.zulaessigesGesamtgewicht || "Nicht verfügbar"}</td>
                </tr>
                <tr>
                    <td>Kupplungstyp</td>
                    <td>{caravan.kupplungstyp || "Nicht verfügbar"}</td>
                </tr>
                <tr>
                    <td>Höchstgeschwindigkeit</td>
                    <td>{caravan.hoechstgeschwindigkeit || "Nicht verfügbar"}</td>
                </tr>
                <tr>
                    <td>Anzahl der Schlafplätze</td>
                    <td>{caravan.anzahlSchlafplaetze || "Nicht verfügbar"}</td>
                </tr>
                </tbody>
            </table>
        </section>
    );
}
