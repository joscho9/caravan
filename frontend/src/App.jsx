import React from 'react';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import Dashboard from './components/Dashboard.jsx';
import Overview from "./components/Overview.jsx"; // Passe den Pfad an, falls nötig
import CaravanDetail from "./components/CaravanDetails.jsx"; // Diese Datei erstellen wir später


function App() {
    return (
        <>
        <Router>
            <Routes>
                <Route path="/" element={<Overview />} />
                <Route path="/caravan/:id" element={<CaravanDetail />} />
            </Routes>
        </Router>
        </>
    );
}

export default App;