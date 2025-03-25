import React from "react";

const TimelineEventCard = ({ type, title, date, personId }) => {
    return (
        <div className="bg-white shadow-lg rounded-lg p-4 w-64">
            <div className="flex items-center space-x-2">
                <span className={`w-3 h-3 rounded-full ${type === "vote" ? "bg-custom-cyan" : "bg-custom-red"}`}></span>
                <span className="text-gray-500 text-sm">{date}</span>
            </div>
            <h3 className="font-semibold text-custom-blue mt-2">{title}</h3>
            <p className="text-gray-600 text-sm">{type === "vote" ? "Vote" : "Bill Topic"}</p>
            {personId && <p className="text-gray-500 text-xs">Person ID: {personId}</p>}
        </div>
    );
};

export default TimelineEventCard;
