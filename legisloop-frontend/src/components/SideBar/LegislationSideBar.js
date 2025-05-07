/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * LegislationSideBar Declaration.
 ****************************************************************
 * Last Updated: April 3, 2025.
 ***************************************************************/
import React, { useState, useEffect } from "react";
import VoteCard from "../Cards/VoteCard";
import {
    BookOpenIcon,
    DocumentDuplicateIcon,
    DocumentTextIcon,
    DropDownArrowIcon,
} from "../Icons/Icons";
import Tooltip from "../ToolTips/ToolTip";
import { NewspaperIcon } from "lucide-react";
import axios from "axios";

function LegislationSideBar({ votes = [], rollCallSummary, billInfo, activeLevel, setActiveLevel, }) {

    // vote summary: use passed‐in or fallback
    const fallback = votes.reduce(
        (acc, v) => {
            if (v.decision === "Yea") acc.yea++;
            else if (v.decision === "Nay") acc.nay++;
            else acc.abstain++;
            return acc;
        },
        { yea: 0, nay: 0, abstain: 0 }
    );
    const summary = rollCallSummary || fallback;

    // get rep names
    const [idToName, setIdToName] = useState({});
    useEffect(() => {
        const ids = Array.from(new Set(votes.map((v) => v.person_id)));
        if (!ids.length) return;

        axios
            .post("/api/v1/representative/names", ids)
            .then((res) => setIdToName(res.data))
            .catch((err) =>
                console.error("Failed to fetch representative names", err)
            );
    }, [votes]);

    const [isLevelOpen, setIsLevelOpen] = useState(false);
    const [isVotingOpen, setIsVotingOpen] = useState(false);

    const levels = [
        { icon: <DocumentTextIcon />, label: "Un-edited", param: "UN_EDITED" },
        { icon: <BookOpenIcon />, label: "Moderate-read", param: "MODERATE" },
        { icon: <NewspaperIcon />, label: "Easy-read", param: "EASY" },
        { icon: <DocumentDuplicateIcon />, label: "1-page", param: "ONE_PAGE" },
    ];

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

            {/* Reading Level */}
            <div className="mb-4">
                <Tooltip
                    text="Adjust the reading level for this document."
                    position="right"
                >
                    <h6 className="text-xl font-semibold cursor-pointer">
                        Reading Level
                    </h6>
                </Tooltip>

                <button
                    onClick={() => setIsLevelOpen((state) => !state)}
                    className="flex items-center justify-between w-full mt-2 p-2 hover:bg-gray-100 rounded"
                >
                    <span>{levels.find((l) => l.param === activeLevel)?.label}</span>
                    <DropDownArrowIcon
                        className={`${isLevelOpen ? "rotate-180" : ""}`}
                    />
                </button>

                <div className={`mt-1 overflow-hidden transition-all ${isLevelOpen ? "max-h-60" : "max-h-0"}`}>
                    {levels.map((lvl) => (
                        <div
                            key={lvl.param}
                            onClick={() => {
                                setActiveLevel(lvl.param);
                                setIsLevelOpen(false);
                            }}
                            className={`flex items-center gap-2 p-2 rounded cursor-pointer ${activeLevel === lvl.param
                                ? "bg-custom-red-light bg-opacity-50 border-l-4 border-custom-red font-semibold"
                                : "hover:bg-gray-100"
                                }`}
                        >
                            {lvl.icon}
                            <span>{lvl.label}</span>
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
                    onClick={() => setIsVotingOpen((o) => !o)}
                    className="flex items-center justify-between w-full p-2 font-semibold hover:bg-gray-100 rounded"
                >
                    <span>Voting Record</span>
                    <DropDownArrowIcon
                        className={`${isVotingOpen ? "rotate-180" : ""}`}
                    />
                </button>

                <div
                    className={`mt-2 transition-[height] duration-300 ease-in-out overflow-hidden ${isVotingOpen ? "h-64" : "h-0"
                        }`}
                >
                    <div className="h-full overflow-y-auto p-2 space-y-2">
                        {votes.length > 0 ? (
                            votes.map((v, i) => {
                                const name = idToName[v.person_id] || `Member ${v.person_id}`;
                                return (
                                    <VoteCard name={name} vote={v.decision} />
                                );
                            })
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

