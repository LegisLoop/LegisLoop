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

function LegislationSideBar({ votes = [], rollCallSummary, billInfo }) {
    // if no precomputed summary, compute from votes array
    const computed = votes.reduce(
        (acc, v) => {
            if (v.decision === "Yea") acc.yea++;
            else if (v.decision === "Nay") acc.nay++;
            else acc.abstain++;
            return acc;
        },
        { yea: 0, nay: 0, abstain: 0 }
    );
    const summary = rollCallSummary || computed;

    const [isLevelOpen, setIsLevelOpen] = useState(false);
    const [isVotingRecordOpen, setIsVotingRecordOpen] = useState(false);
    const [activeLevel, setActiveLevel] = useState("Easy");

    return (
        <div className="w-full lg:w-[20rem] lg:min-h-screen flex flex-col bg-white p-4 text-custom-blue shadow-xl shadow-blue-gray-900/5">
            {/* Bill status */}
            {billInfo && (
                <div className="mb-4 p-2 bg-gray-50 rounded">
                    <p className="text-sm text-gray-600">
                        Status: {billInfo.status} • Introduced:{" "}
                        {new Date(billInfo.dateIntroduced).toLocaleDateString()}
                    </p>
                </div>
            )}

            <hr className="my-2 border-blue-gray-50" />

            {/* Reading Level Section */}
            <div className="mb-4">
                <Tooltip
                    text="Use this sidebar to explore the legislation, adjust the reading level, and view vote details."
                    position="bottom"
                >
                    <h6 className="text-xl font-semibold cursor-pointer">
                        Reading Level
                    </h6>
                </Tooltip>

                <button
                    onClick={() => setIsLevelOpen(!isLevelOpen)}
                    className="flex items-center justify-between w-full mt-2 p-2 hover:bg-gray-100 rounded"
                >
                    <span>{activeLevel}</span>
                    <DropDownArrowIcon
                        className={`${isLevelOpen ? "rotate-180" : ""}`}
                    />
                </button>
                <div
                    className={`mt-1 overflow-hidden transition-all ${isLevelOpen ? "max-h-60" : "max-h-0"
                        }`}
                >
                    {[
                        { icon: <DocumentTextIcon />, label: "Un-edited" },
                        { icon: <BookOpenIcon />, label: "Moderate-read" },
                        { icon: <NewspaperIcon />, label: "Easy-read" },
                        { icon: <DocumentDuplicateIcon />, label: "1-page" },
                    ].map((item) => (
                        <div
                            key={item.label}
                            onClick={() => setActiveLevel(item.label)}
                            className={`flex items-center gap-2 p-2 rounded ${activeLevel === item.label
                                ? "bg-custom-red-light bg-opacity-50 border-l-4 border-custom-red"
                                : "hover:bg-gray-100"
                                }`}
                        >
                            {item.icon}
                            <span>{item.label}</span>
                        </div>
                    ))}
                </div>
            </div>

            {/* Vote Summary */}
            <div className="p-4 bg-gray-100 rounded-lg mb-4">
                <h6 className="text-lg font-bold text-custom-blue">Vote Summary</h6>
                <ul className="mt-2 space-y-1 text-sm text-gray-700">
                    <li>
                        <span className="font-semibold text-custom-cyan">Yea:</span>{" "}
                        {summary.yea}
                    </li>
                    <li>
                        <span className="font-semibold text-custom-red">Nay:</span>{" "}
                        {summary.nay}
                    </li>
                    <li>
                        <span className="font-semibold text-custom-blue">Abstain:</span>{" "}
                        {summary.abstain}
                    </li>
                </ul>
            </div>

            {/* Voting Record */}
            <div>
                <button
                    onClick={() => setIsVotingRecordOpen(!isVotingRecordOpen)}
                    className="flex items-center justify-between w-full p-2 font-semibold hover:bg-gray-100 rounded"
                >
                    <span>Voting Record</span>
                    <DropDownArrowIcon
                        className={`${isVotingRecordOpen ? "rotate-180" : ""}`}
                    />
                </button>

                <div className={`mt-2 transition-[height] duration-300 ease-in-out overflow-hidden ${isVotingRecordOpen ? "h-64" : "h-0"}`}>
                    <div className="h-full overflow-y-auto p-2 space-y-2">
                        {votes.length > 0 ? (
                            votes.map((v, index) => (
                                <VoteCard key={index} name={v.representative} vote={v.decision} />
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

