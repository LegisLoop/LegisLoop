/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * LegislationSideBar Declaration.
 ****************************************************************
 * Last Updated: April 3, 2025.
 ***************************************************************/
import { useState } from "react";
import VoteCard from "../Cards/VoteCard";
import {
    BookOpenIcon,
    DocumentDuplicateIcon,
    DocumentTextIcon,
    DropDownArrowIcon,
    MapPinIcon,
} from "../Icons/Icons";
import Tooltip from "../ToolTips/ToolTip";
import { NewspaperIcon } from "lucide-react";

function LegislationSideBar({ votes = [] }) {
    // Summarize votes
    const voteSummary = votes.reduce(
        (acc, vote) => {
            if (vote.decision === "Yea") acc.yea++;
            else if (vote.decision === "Nay") acc.nay++;
            else acc.abstain++;
            return acc;
        },
        { yea: 0, nay: 0, abstain: 0 }
    );

    // Drop-downs start closed
    const [isLevelOpen, setIsLevelOpen] = useState(false);
    const [isVotingRecordOpen, setIsVotingRecordOpen] = useState(false);

    // Track which reading level is active
    const [activeLevel, setActiveLevel] = useState("Easy");

    const handleLevelClick = (level) => {
        setActiveLevel(level);
    };

    return (
        // On mobile/tablet: w-full, no forced min-height
        // On lg+: fix width & fill vertical space
        <div className="w-full lg:w-[20rem] min-h-auto lg:min-h-screen flex flex-col bg-white p-4 text-custom-blue shadow-xl shadow-blue-gray-900/5">
            <div className="flex items-center gap-2 p-4">
                <Tooltip
                    text="Use this sidebar to explore the legislation, adjust the reading level, and view vote details."
                    position="bottom"
                >
                    <h5 className="text-xl font-sans font-semibold leading-snug text-custom-blue cursor-pointer">
                        Legislation Overview &amp; Reading Level
                    </h5>
                </Tooltip>
            </div>
            <hr className="my-2 border-blue-gray-50" />

            {/* Reading Level Section */}
            <nav className="flex flex-col gap-1 p-2 font-sans text-base font-normal text-blue-gray-700">
                <div className="relative block w-full">
                    <button
                        type="button"
                        onClick={() => setIsLevelOpen(!isLevelOpen)}
                        className="flex items-center justify-between w-full p-3 font-sans text-xl font-semibold leading-snug text-left transition-all duration-300 ease-in-out border-b-0 select-none border-b-blue-gray-100 text-blue-gray-700 hover:bg-gray-200"
                    >
                        <div className="grid mr-4 place-items-center">
                            <MapPinIcon className="text-custom-red" />
                        </div>
                        <p className="block mr-auto font-sans text-base font-bold leading-relaxed text-blue-gray-900">
                            Reading Level
                        </p>
                        <span className="ml-4">
              <DropDownArrowIcon
                  className={`${isLevelOpen ? "rotate-180" : ""}`}
              />
            </span>
                    </button>

                    <div
                        className={`overflow-hidden transition-all duration-300 ease-in-out ${
                            isLevelOpen ? "max-h-96 opacity-100" : "max-h-0 opacity-0"
                        }`}
                    >
                        <div className="block w-full py-1 font-sans text-sm font-light leading-normal text-gray-700">
                            <nav className="flex flex-col gap-1 p-0">
                                {[
                                    { icon: <DocumentTextIcon />, label: "Un-edited" },
                                    { icon: <BookOpenIcon />, label: "Moderate-read" },
                                    { icon: <NewspaperIcon />, label: "Easy-read" },
                                    { icon: <DocumentDuplicateIcon />, label: "1-page" },
                                ].map((item, index) => (
                                    <div
                                        key={index}
                                        onClick={() => handleLevelClick(item.label)}
                                        className={`flex items-center w-full p-3 transition-all rounded-lg outline-none text-start ${
                                            activeLevel === item.label
                                                ? "bg-custom-red-light bg-opacity-50 border-l-4 border-custom-red text-custom-red-dark font-semibold"
                                                : "hover:bg-gray-200"
                                        }`}
                                    >
                                        <div
                                            className={`grid mr-4 place-items-center ${
                                                activeLevel === item.label
                                                    ? "text-custom-red-dark"
                                                    : "text-custom-blue"
                                            }`}
                                        >
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

            {/* Vote Summary */}
            <div className="p-4 bg-gray-100 rounded-lg">
                <h6 className="text-lg font-bold text-custom-blue">Vote Summary</h6>
                <ul className="mt-2 text-sm text-gray-700">
                    <li>
                        <span className="font-semibold text-custom-cyan">Yea:</span>{" "}
                        {voteSummary.yea}
                    </li>
                    <li>
                        <span className="font-semibold text-custom-red">Nay:</span>{" "}
                        {voteSummary.nay}
                    </li>
                    <li>
                        <span className="font-semibold text-custom-blue">Abstain:</span>{" "}
                        {voteSummary.abstain}
                    </li>
                </ul>
            </div>

            {/* Voting Record */}
            <div className="relative block w-full mt-4">
                <button
                    type="button"
                    onClick={() => setIsVotingRecordOpen(!isVotingRecordOpen)}
                    className="flex items-center justify-between w-full p-3 font-sans text-xl font-semibold leading-snug text-left transition-all duration-300 ease-in-out border-b-0 select-none border-b-blue-gray-100 text-blue-gray-700 hover:bg-gray-200"
                >
                    <div className="grid mr-4 place-items-center">
                        <MapPinIcon className="text-custom-red" />
                    </div>
                    <p className="block mr-auto font-sans text-base font-bold leading-relaxed text-blue-gray-900">
                        Voting Record
                    </p>
                    <span className="ml-4">
            <DropDownArrowIcon
                className={`${isVotingRecordOpen ? "rotate-180" : ""}`}
            />
          </span>
                </button>

                <div
                    className={`overflow-hidden transition-all duration-300 ease-in-out ${
                        isVotingRecordOpen ? "max-h-96 opacity-100" : "max-h-0 opacity-0"
                    }`}
                >
                    <div className="flex flex-col gap-2 mt-2">
                        {votes.length > 0 ? (
                            votes.map((vote, index) => (
                                <VoteCard key={index} name={vote.representative} vote={vote.decision} />
                            ))
                        ) : (
                            <p className="text-gray-500 text-sm">No votes available</p>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default LegislationSideBar;
