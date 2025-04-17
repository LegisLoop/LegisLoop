/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * LandingSideBar declaration.
 ****************************************************************
 * Last Updated: February 19, 2025.
 ***************************************************************/
import { useState } from "react";
import {
    CompassIcon,
    DropDownArrowIcon,
    LibraryIcon,
    FilterIcon,
    MapPinIcon,
    MapIcon,
} from "../Icons/Icons";
import Tooltip from "../ToolTips/ToolTip";
import policyMap from "../../constants/policyMap";

function LandingSideBar({ activeLevel, setActiveLevel, activePolicy, setActivePolicy }) {
    const [isLevelOpen, setIsLevelOpen] = useState(true);
    const [isPolicyOpen, setIsPolicyOpen] = useState(true);

    const handleLevelClick = (level) => {
        setActiveLevel(level);
    };

    const handlePolicyClick = (policy) => {
        setActivePolicy((prevActivePolicy) =>
            prevActivePolicy === policy ? null : policy
        );
    };

    return (
        <>
            <div
                className="relative flex h-full w-full max-w-[20rem] flex-col bg-white bg-clip-border p-4 text-custom-blue shadow-xl shadow-blue-gray-900/5 z-9">

                <div className="flex items-center gap-2 p-4">
                    <CompassIcon />
                    <Tooltip text="Use this sidebar to filter and explore legislation by government level and policy area." position="bottom">
                        <h5 className="text-xl font-sans font-semibold leading-snug text-blue-gray-900 cursor-pointer">
                            Legislative Explorer
                        </h5>
                    </Tooltip>
                </div>
                <hr className="my-2 border-blue-gray-50" />
                <nav
                    className="flex min-w-[240px] flex-col gap-1 p-2 font-sans text-base font-normal text-blue-gray-700">
                    <div className="relative block w-full">
                        <div role="button"
                            className="flex items-center w-full p-0 leading-tight transition-all rounded-lg outline-none text-start">
                            <button type="button" onClick={() => setIsLevelOpen(!isLevelOpen)}
                                className="flex items-center justify-between w-full p-3 font-sans text-xl antialiased font-semibold leading-snug text-left transition-all duration-300 ease-in-out border-b-0 select-none border-b-blue-gray-100 text-blue-gray-700 hover:bg-gray-200">
                                <div className="grid mr-4 place-items-center">
                                    <MapPinIcon className="text-custom-red" />
                                </div>
                                <p className="block mr-auto font-sans text-base antialiased font-bold leading-relaxed text-blue-gray-900">
                                    Government Level
                                </p>
                                <span className="ml-4">
                                    <DropDownArrowIcon />
                                </span>
                            </button>
                        </div>
                        <div className={`overflow-hidden transition-all duration-300 ease-in-out ${isLevelOpen ? 'max-h-96 opacity-100' : 'max-h-0 opacity-0'}`}>
                            <div
                                className="block w-full py-1 font-sans text-sm antialiased font-light leading-normal text-gray-700">
                                <nav
                                    className="flex min-w-[240px] flex-col gap-1 p-0 font-sans text-base font-normal text-blue-gray-700">
                                    {[
                                        { icon: <LibraryIcon />, label: "Federal" },
                                        { icon: <MapIcon />, label: "State" },
                                        // { icon: <BuildingIcon />, label: "Local" }
                                    ].map((item, index) => (
                                        <div key={index} role="button"
                                            onClick={() => handleLevelClick(item.label)}
                                            className={`flex items-center w-full p-3 leading-tight transition-all rounded-lg outline-none text-start ${activeLevel === item.label ? "bg-custom-red-light bg-opacity-50 border-l-4 border-custom-red text-custom-red-dark font-semibold" : "hover:bg-gray-200"
                                                }`}>
                                            <div className={`grid mr-4 place-items-center ${activeLevel === item.label ? "text-custom-red-dark" : "text-custom-blue"
                                                }`}>
                                                {item.icon}
                                            </div>
                                            {item.label}
                                        </div>
                                    ))}
                                </nav>
                            </div>
                        </div>
                    </div>
                    <div className="relative block w-full">
                        <div role="button"
                            className="flex items-center w-full p-0 leading-tight transition-all rounded-lg outline-none text-start">
                            <button type="button" onClick={() => setIsPolicyOpen(!isPolicyOpen)}
                                className="flex items-center justify-between w-full p-3 font-sans text-xl antialiased font-semibold leading-snug text-left transition-all duration-300 ease-in-out border-b-0 select-none border-b-blue-gray-100 text-blue-gray-700 hover:bg-gray-200">
                                <div className="grid mr-4 place-items-center">
                                    <FilterIcon className="text-custom-red" />
                                </div>
                                <p className="block mr-auto font-sans text-base antialiased font-bold leading-relaxed text-blue-gray-900">
                                    Policy Areas
                                </p>
                                <span className="ml-4">
                                    <DropDownArrowIcon />
                                </span>
                            </button>
                        </div>
                        <div className={`overflow-hidden duration-300 ease-in-out ${isPolicyOpen ? 'max-h-100 opacity-100' : 'max-h-0 opacity-0'}`}>
                            <div
                                className="block w-full py-1 font-sans text-sm antialiased font-light leading-normal text-gray-700">
                                <nav
                                    className="flex min-w-[240px] flex-col gap-1 p-0 font-sans text-base font-normal text-blue-gray-700">
                                    {policyMap.map((item) => (
                                        <div key={item.key} role="button"
                                            onClick={() => handlePolicyClick(item.key)}
                                            className={`flex items-center w-full p-3 leading-tight transition-all rounded-lg outline-none text-start ${activePolicy === item.key ? "bg-custom-red-light bg-opacity-50 border-l-4 border-custom-red text-custom-red-dark font-semibold" : "hover:bg-gray-200"
                                                }`}>
                                            <div className={`grid mr-4 place-items-center ${activePolicy === item.key ? "text-custom-red-dark" : "text-custom-blue"
                                                }`}>
                                                {item.icon}
                                            </div>
                                            {item.label}
                                        </div>
                                    ))}
                                </nav>
                            </div>
                        </div>
                    </div>
                </nav>
            </div>
        </>
    );
}

export default LandingSideBar;