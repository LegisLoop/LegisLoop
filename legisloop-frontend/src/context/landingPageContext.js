import { createContext, useState, useContext } from "react";

const LandingPageContext = createContext();

export function useLandingPage() {
    return useContext(LandingPageContext);
}

export function LandingPageProvider({ children }) {
    const [activeLevel, setActiveLevel] = useState("Federal");
    const [activePolicy, setActivePolicy] = useState(null);
    const [activeStateId, setActiveStateId] = useState(52);
    const [pageNumber, setPageNumber] = useState(0);
    const [locationRequested, setLocationRequested] = useState(false);
    const [searchTerm, setSearchTerm] = useState("");

    const value = {
        activeLevel,
        setActiveLevel,
        activePolicy,
        setActivePolicy,
        activeStateId,
        setActiveStateId,
        pageNumber,
        setPageNumber,
        locationRequested,
        setLocationRequested,
        searchTerm,
        setSearchTerm,
    };

    return (
        <LandingPageContext.Provider value={value}>
            {children}
        </LandingPageContext.Provider>
    );
}