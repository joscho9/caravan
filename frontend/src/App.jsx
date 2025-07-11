import React from 'react';
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import Overview from "./pages/Overview.jsx"; 
import CaravanDetail from "./pages/CaravanDetails.jsx";
import Impressum from "./pages/Impressum.jsx"


function App() {
    return (
        <>
        <Router>
            <Routes>
                <Route path="/" element={<Overview />} />
                <Route path="/caravan/:id" element={<CaravanDetail />} />
                <Route path="/impressum" element={<Impressum />} />
            </Routes>
        </Router>
        </>
    );
}

export default App;