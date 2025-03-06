/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * StateSelectorBar Declaration.
 ****************************************************************
 * Last Updated: February 21, 2025.
 ***************************************************************/
import React, { useState } from 'react';

const states = [
    { name: 'Alabama', abbr: 'AL' }, { name: 'Alaska', abbr: 'AK' }, { name: 'Arizona', abbr: 'AZ' }, { name: 'Arkansas', abbr: 'AR' },
    { name: 'California', abbr: 'CA' }, { name: 'Colorado', abbr: 'CO' }, { name: 'Connecticut', abbr: 'CT' }, { name: 'Delaware', abbr: 'DE' },
    { name: 'Florida', abbr: 'FL' }, { name: 'Georgia', abbr: 'GA' }, { name: 'Hawaii', abbr: 'HI' }, { name: 'Idaho', abbr: 'ID' },
    { name: 'Illinois', abbr: 'IL' }, { name: 'Indiana', abbr: 'IN' }, { name: 'Iowa', abbr: 'IA' }, { name: 'Kansas', abbr: 'KS' },
    { name: 'Kentucky', abbr: 'KY' }, { name: 'Louisiana', abbr: 'LA' }, { name: 'Maine', abbr: 'ME' }, { name: 'Maryland', abbr: 'MD' },
    { name: 'Massachusetts', abbr: 'MA' }, { name: 'Michigan', abbr: 'MI' }, { name: 'Minnesota', abbr: 'MN' }, { name: 'Mississippi', abbr: 'MS' },
    { name: 'Missouri', abbr: 'MO' }, { name: 'Montana', abbr: 'MT' }, { name: 'Nebraska', abbr: 'NE' }, { name: 'Nevada', abbr: 'NV' },
    { name: 'New Hampshire', abbr: 'NH' }, { name: 'New Jersey', abbr: 'NJ' }, { name: 'New Mexico', abbr: 'NM' }, { name: 'New York', abbr: 'NY' },
    { name: 'North Carolina', abbr: 'NC' }, { name: 'North Dakota', abbr: 'ND' }, { name: 'Ohio', abbr: 'OH' }, { name: 'Oklahoma', abbr: 'OK' },
    { name: 'Oregon', abbr: 'OR' }, { name: 'Pennsylvania', abbr: 'PA' }, { name: 'Rhode Island', abbr: 'RI' }, { name: 'South Carolina', abbr: 'SC' },
    { name: 'South Dakota', abbr: 'SD' }, { name: 'Tennessee', abbr: 'TN' }, { name: 'Texas', abbr: 'TX' }, { name: 'Utah', abbr: 'UT' },
    { name: 'Vermont', abbr: 'VT' }, { name: 'Virginia', abbr: 'VA' }, { name: 'Washington', abbr: 'WA' }, { name: 'West Virginia', abbr: 'WV' },
    { name: 'Wisconsin', abbr: 'WI' }, { name: 'Wyoming', abbr: 'WY' }
];

const StateSelectorBar = ({ onSelect = () => {} }) => {
    const [selectedState, setSelectedState] = useState(null);

    const handleStateClick = (state) => {
        setSelectedState(state);
        onSelect(state);
    };

    return (
        <div className="pt-2 border-b border-gray-300 bg-gray-100 text-center">
            <h2 className="text-sm font-semibold text-gray-700 mb-2">Select a state to view its senators:</h2>
            <div className="flex overflow-x-auto whitespace-nowrap justify-center items-center gap-2 p-4 shadow-md bg-white">
                {states.map((state) => (
                    <button
                        key={state.abbr}
                        onClick={() => handleStateClick(state)}
                        className={`w-8 h-8 text-xs font-sans font-medium rounded-full transition-all duration-300 flex items-center justify-center ${selectedState?.abbr === state.abbr ? 'bg-custom-red-light text-custom-red-dark' : 'bg-white text-gray-700 border border-gray-300 hover:bg-gray-200'}`}
                    >
                        {state.abbr}
                    </button>
                ))}
            </div>
        </div>
    );
};

export default StateSelectorBar;
