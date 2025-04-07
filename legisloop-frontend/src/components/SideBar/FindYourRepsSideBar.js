import { useState } from "react";
import {
    CompassIcon,
    DropDownArrowIcon,
    LibraryIcon,
    MapPinIcon,
    BuildingIcon,
    MapIcon,
    AdjustmentsIcon,
} from "../Icons/Icons";
import Tooltip from "../ToolTips/ToolTip";

const stateMap = {
    "Alabama": 1, "Alaska": 2, "Arizona": 3, "Arkansas": 4, "California": 5,
    "Colorado": 6, "Connecticut": 7, "Delaware": 8, "Florida": 9, "Georgia": 10,
    "Hawaii": 11, "Idaho": 12, "Illinois": 13, "Indiana": 14, "Iowa": 15,
    "Kansas": 16, "Kentucky": 17, "Louisiana": 18, "Maine": 19, "Maryland": 20,
    "Massachusetts": 21, "Michigan": 22, "Minnesota": 23, "Mississippi": 24, "Missouri": 25,
    "Montana": 26, "Nebraska": 27, "Nevada": 28, "New Hampshire": 29, "New Jersey": 30,
    "New Mexico": 31, "New York": 32, "North Carolina": 33, "North Dakota": 34, "Ohio": 35,
    "Oklahoma": 36, "Oregon": 37, "Pennsylvania": 38, "Rhode Island": 39, "South Carolina": 40,
    "South Dakota": 41, "Tennessee": 42, "Texas": 43, "Utah": 44, "Vermont": 45,
    "Virginia": 46, "Washington": 47, "West Virginia": 48, "Wisconsin": 49, "Wyoming": 50,
    "Washington D.C.": 51, "United States": 52
};

function RepresentativeSideBar({ setStateId }) {
    const [activeLevel, setActiveLevel] = useState(null);
    const [selectedState, setSelectedState] = useState("");
    const [selectedCity, setSelectedCity] = useState("");
    const [selectedCounty, setSelectedCounty] = useState("");
    const [isLevelOpen, setIsLevelOpen] = useState(false);
    const [, setIsLocalOpen] = useState(false);

    const handleStateSelection = (state) => {
        setSelectedState(state);
        setStateId(stateMap[state]); // Update state ID in parent
    };

    const handleLevelClick = (level) => {
        setActiveLevel(level);
        if (level === "Local") {
            setIsLocalOpen(true);
        } else {
            setIsLocalOpen(false);
        }
    };

    return (
        <div className="relative flex h-full w-full max-w-[20rem] flex-col bg-white p-4 text-custom-blue shadow-xl shadow-blue-gray-900/5 z-9">
            {/* Header */}
            <div className="flex items-center gap-2 p-2 md:p-4">
                <CompassIcon />
                <Tooltip text="Find your representatives by location and government level." position="bottom">
                    <h5 className="text-lg md:text-xl font-sans font-semibold leading-snug text-blue-gray-900 cursor-pointer">
                        Legislator Finder
                    </h5>
                </Tooltip>
            </div>

            <hr className="my-2 border-blue-gray-50" />

            {/* LOCATION SELECTOR */}
            <div className="relative w-full">
                <button
                    type="button"
                    className="flex items-center justify-between w-full p-2 md:p-3 text-lg md:text-xl font-semibold leading-snug text-left transition-all duration-300 border-b border-gray-300 hover:bg-gray-200"
                >
                    <div className="grid mr-2 md:mr-4 place-items-center">
                        <MapPinIcon className="text-custom-red" />
                    </div>
                    <p className="mr-auto text-base md:text-lg font-bold">Select Location</p>
                    <span className="ml-2 md:ml-4"><DropDownArrowIcon /></span>
                </button>

                {/* Dropdown with Search */}
                <div className="py-1 text-sm text-gray-700">
                    <input
                        type="text"
                        placeholder="Search state..."
                        value={selectedState}
                        onChange={(e) => setSelectedState(e.target.value)}
                        className="w-full p-2 border border-gray-300 rounded-lg bg-white text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-400"
                    />
                    <ul className="max-h-48 overflow-auto mt-2">
                        {Object.keys(stateMap)
                            .filter(state => state.toLowerCase().includes(selectedState.toLowerCase()))
                            .map((state, index) => (
                                <li
                                    key={index}
                                    onClick={() => handleStateSelection(state)}
                                    className="p-2 cursor-pointer hover:bg-gray-200"
                                >
                                    {state}
                                </li>
                            ))}
                    </ul>
                </div>
            </div>

            {/* GOVERNMENT LEVEL SELECTOR */}
            {selectedState && (
                <div className="relative w-full mt-4">
                    <button
                        type="button"
                        onClick={() => setIsLevelOpen(!isLevelOpen)}
                        className="flex items-center justify-between w-full p-2 md:p-3 text-lg md:text-xl font-semibold leading-snug text-left transition-all duration-300 border-b border-gray-300 hover:bg-gray-200"
                    >
                        <div className="grid mr-2 md:mr-4 place-items-center">
                            <AdjustmentsIcon className="text-custom-red" />
                        </div>
                        <p className="mr-auto text-base md:text-lg font-bold">Select Government Level</p>
                        <span className="ml-2 md:ml-4">
                            <DropDownArrowIcon />
                        </span>
                    </button>

                    <div className={`overflow-hidden transition-all duration-300 ${isLevelOpen ? "max-h-96 opacity-100" : "max-h-0 opacity-0"}`}>
                        <div className="py-1 text-sm text-gray-700">
                            <nav className="flex flex-col gap-1">
                                {[
                                    { icon: <LibraryIcon />, label: "Federal" },
                                    { icon: <MapIcon />, label: "State" },
                                    { icon: <BuildingIcon />, label: "Local" },
                                ].map((item, index) => (
                                    <button
                                        key={index}
                                        onClick={() => handleLevelClick(item.label)}
                                        className={`flex items-center w-full p-2 md:p-3 transition-all rounded-lg text-start ${
                                            activeLevel === item.label
                                                ? "bg-custom-red-light bg-opacity-50 border-l-4 border-custom-red text-custom-red-dark font-semibold"
                                                : "hover:bg-gray-200"
                                        }`}
                                    >
                                        <div className={`grid mr-2 md:mr-4 place-items-center ${
                                            activeLevel === item.label ? "text-custom-red-dark" : "text-custom-blue"
                                        }`}>
                                            {item.icon}
                                        </div>
                                        {item.label}
                                    </button>
                                ))}
                            </nav>
                        </div>
                    </div>
                </div>
            )}

            {/* LOCAL FILTERS: CITY / COUNTY */}
            {activeLevel === "Local" && (
                <div className="relative w-full mt-4">
                    <div className="py-2">
                        <label className="block text-sm font-semibold text-gray-700 mb-1">
                            Search by City
                        </label>
                        <input
                            type="text"
                            placeholder="Enter city..."
                            value={selectedCity}
                            onChange={(e) => setSelectedCity(e.target.value)}
                            className="w-full p-2 border border-gray-300 rounded-lg bg-white text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-400"
                        />
                    </div>

                    <div className="py-2">
                        <label className="block text-sm font-semibold text-gray-700 mb-1">
                            Search by County
                        </label>
                        <input
                            type="text"
                            placeholder="Enter county..."
                            value={selectedCounty}
                            onChange={(e) => setSelectedCounty(e.target.value)}
                            className="w-full p-2 border border-gray-300 rounded-lg bg-white text-gray-700 focus:outline-none focus:ring-2 focus:ring-blue-400"
                        />
                    </div>
                </div>
            )}
        </div>
    );
}

export default RepresentativeSideBar;
