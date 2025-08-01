import React from 'react';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import Overview from "./pages/Overview.jsx"; 
import CaravanDetail from "./pages/CaravanDetails.jsx";
import Willkommen from "./pages/Willkommen.jsx";
import Impressum from "./pages/Impressum.jsx";
import Datenschutz from "./pages/Datenschutz.jsx";
import Kontakt from "./pages/Kontakt.jsx";


function App() {
    return (
        <>
        <Router>
            <Routes>
                <Route path="/" element={<Overview />} />
                <Route path="/caravan/:id" element={<CaravanDetail />} />
                <Route path="/willkommen" element={<Willkommen />} />
                <Route path="/impressum" element={<Impressum />} />
                <Route path="/datenschutz" element={<Datenschutz />} />
                <Route path="/kontakt" element={<Kontakt />} />
            </Routes>
        </Router>
        </>
    );
}

export default App;