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

const StateWizardSelector = ({ onSelect = () => {} }) => {
    const [step, setStep] = useState(1);
    const [selectedState, setSelectedState] = useState(null);

    const handleStateSelection = (state) => {
        setSelectedState(state);
        setStep(2);
    };

    return (
        <div className="max-w-lg mx-auto bg-white p-6 shadow-md rounded-lg">
            {step === 1 && (
                <div className="text-center">
                    <h2 className="text-lg font-semibold mb-4">Select Your State</h2>
                    <div className="grid grid-cols-6 gap-2">
                        {states.map((state) => (
                            <button
                                key={state.abbr}
                                onClick={() => handleStateSelection(state)}
                                className="px-4 py-2 text-sm font-medium bg-gray-100 rounded-lg hover:bg-blue-500 hover:text-white"
                            >
                                {state.abbr}
                            </button>
                        ))}
                    </div>
                </div>
            )}

            {step === 2 && (
                <div className="text-center">
                    <h2 className="text-lg font-semibold mb-4">You Selected {selectedState?.name}</h2>
                    <p className="text-gray-600">Now fetching representatives for {selectedState?.name}...</p>
                    <button
                        className="mt-4 px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
                        onClick={() => setStep(1)}
                    >
                        Change State
                    </button>
                </div>
            )}
        </div>
    );
};

export default StateWizardSelector;
