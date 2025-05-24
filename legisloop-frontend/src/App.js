/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * App declaration.
 ****************************************************************
 * Last Updated: February 18, 2025.
 ***************************************************************/
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import LandingPage from "./pages/LandingPage";
import LegislationPage from "./pages/LegislationPage";
import RepresentativePage from "./pages/RepresentativePage";
import ConnectPage from "./pages/ConnectPage";
import FindYourRepresentativesPage from "./pages/FindYourRepresentativePage";
import AboutPage from "./pages/AboutPage";
import { LandingPageProvider } from "./context/landingPageContext";

function App() {
    return (
        <Router>
            <LandingPageProvider>
                <Routes>
                    <Route path="/" element={<LandingPage />} />
                    <Route path="/legislation/:id" element={<LegislationPage />} />
                    <Route path="/findrepresentatives" element={<FindYourRepresentativesPage />} />
                    <Route path="/representative/:id" element={<RepresentativePage />} />
                    <Route path="/connect" element={<ConnectPage />} />
                    <Route path="/aboutus" element={<AboutPage />} />
                </Routes>
            </LandingPageProvider>

        </Router>
    );
}

export default App;
