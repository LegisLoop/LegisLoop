import { useState } from "react";
import VoteCard from "../Cards/VoteCard";
import { CompassIcon } from "../Icons/Icons";
import Tooltip from "../ToolTips/ToolTip";

function LegislationSideBar({ votes = [] }) {
    // Count votes
    const voteSummary = votes.reduce(
        (acc, vote) => {
            if (vote.decision === "Yea") acc.yea++;
            else if (vote.decision === "Nay") acc.nay++;
            else acc.abstain++;
            return acc;
        },
        { yea: 0, nay: 0, abstain: 0 }
    );

    return (
        <div className="relative flex h-full w-full max-w-[20rem] flex-col bg-white bg-clip-border p-4 text-custom-blue shadow-xl shadow-blue-gray-900/5 z-9">
            {/* Sidebar Header */}
            <div className="flex items-center gap-2 p-4">
                <Tooltip
                    text="Use this sidebar to explore votes on this legislation."
                    position="bottom"
                >
                    <h5 className="text-xl font-sans font-semibold leading-snug text-custom-blue cursor-pointer">
                        Legislative Vote Overview
                    </h5>
                </Tooltip>
            </div>
            <hr className="my-2 border-blue-gray-50" />

            {/* Vote Summary */}
            <div className="p-4 bg-gray-100 rounded-lg">
                <h6 className="text-lg font-bold text-custom-blue">Vote Summary</h6>
                <ul className="mt-2 text-sm text-gray-700">
                    <li><span className="font-semibold text-custom-cyan">Yea:</span> {voteSummary.yea}</li>
                    <li><span className="font-semibold text-custom-red">Nay:</span> {voteSummary.nay}</li>
                    <li><span className="font-semibold text-custom-blue">Abstain:</span> {voteSummary.abstain}</li>
                </ul>
            </div>

            {/* Voting Record */}
            <h6 className="mt-4 text-lg font-bold text-custom-blue">Voting Record</h6>
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
    );
}

export default LegislationSideBar;
