import { useState } from "react";
import {
    CompassIcon,
    MapPinIcon
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
    const [selectedState, setSelectedState] = useState("");

    const handleStateSelection = (state) => {
        setSelectedState(state);
        setStateId(stateMap[state]);
    };

    return (
        <div className="relative flex flex-col w-full h-screen md:h-full bg-white p-4 text-custom-blue shadow-xl shadow-blue-gray-900/5 z-9">
            {/* Header */}
            <div className="flex items-center gap-2 p-2 md:p-4">
                <CompassIcon />
                <Tooltip text="Find your representatives by location and government level." position="right">
                    <h5 className="text-lg md:text-xl pr-5 font-sans font-semibold leading-snug text-blue-gray-900 cursor-pointer">
                        Legislator Finder
                    </h5>
                </Tooltip>
            </div>

            <hr className="my-2 border-blue-gray-50" />
            <div className="w-full">
                <div className="flex items-center gap-2 mt-5 mb-2">
                    <MapPinIcon className="text-custom-red" />
                    <p className="text-base md:text-lg font-bold">Select Location</p>
                </div>
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
    );
}

export default RepresentativeSideBar;
