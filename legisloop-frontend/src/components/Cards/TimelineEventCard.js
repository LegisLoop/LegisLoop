import React from "react";

const TimelineEventCard = ({ type, title, date, personId, position }) => {
    return (
        <div className="bg-white shadow-lg rounded-lg p-4 w-64">
            <div className="flex items-center space-x-2">
                <span className={`w-3 h-3 rounded-full ${type === "vote" ? "bg-custom-cyan" : "bg-custom-red"}`}></span>
                <span className="text-gray-500 text-sm">{date}</span>
            </div>
            <h3 className="font-semibold text-custom-blue mt-2">{type === "vote" ? ("Voted '" + `${position}` + "'") : "Sponsored Bill"}</h3>
            <p className="text-gray-600 text-sm">{title}</p>
            {personId && <p className="text-gray-500 text-xs">Person ID: {personId}</p>}
        </div>
    );
};

export default TimelineEventCard;
