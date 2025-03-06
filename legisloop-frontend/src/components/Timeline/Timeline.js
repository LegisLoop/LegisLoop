/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * Timeline Declaration.
 ****************************************************************
 * Last Updated: February 21, 2025.
 ***************************************************************/
import React, { useState, useLayoutEffect, useRef } from "react";
import { DocumentTextIcon } from "../Icons/Icons";
import Tooltip from "../ToolTips/ToolTip";

const events = [
    { date: "MAR 2024", title: "Bill Title", type: "vote" },
    { date: "MAR 2024", title: "Bill Title", type: "bill" },
    { date: "JAN 2024", title: "Bill Title", type: "vote" },
    { date: "OCT 2023", title: "Bill Title", type: "bill" },
    { date: "JUN 2023", title: "Bill Title", type: "vote" },
    { date: "MAR 2023", title: "Bill Title", type: "bill" },
    { date: "MAR 2023", title: "Bill Title", type: "bill" },
    { date: "MAR 2023", title: "Bill Title", type: "bill" },
    { date: "MAR 2023", title: "Bill Title", type: "bill" }
];

const Timeline = () => {
    const [showAll, setShowAll] = useState(false);
    const visibleEvents = showAll ? events : events.slice(0, 5);
    const timelineRef = useRef(null);
    const lastEventRef = useRef(null);
    const [timelineHeight, setTimelineHeight] = useState("5rem");

    useLayoutEffect(() => {
        if (lastEventRef.current && timelineRef.current) {
            const timelineTop = timelineRef.current.getBoundingClientRect().top;
            const lastEventBottom = lastEventRef.current.getBoundingClientRect().bottom;
            setTimelineHeight(`${lastEventBottom - timelineTop}px`);
        }
    }, [visibleEvents]); // Runs immediately when visibleEvents change

    return (
        <div className="relative max-w-4xl mx-auto p-6">
            <div className="relative flex items-center text-center pb-4 gap-4">
                <DocumentTextIcon />
                <Tooltip text="View legislative decisions, bill proposals, and voting records in one timeline." position="bottom">
                    <h5 className="text-xl font-sans font-semibold leading-snug text-custom-blue cursor-pointer">
                        Legislative Activity Tracker
                    </h5>
                </Tooltip>
            </div>
            <hr className="my-2 border-blue-gray-50" />
            <div
                ref={timelineRef}
                className="absolute left-1/2 transform -translate-x-1/2 w-1 bg-gray-300 transition-all duration-500"
                style={{ top: "5rem", height: timelineHeight }}
            ></div>

            {visibleEvents.map((event, index) => (
                <div
                    key={index}
                    className="flex items-center mb-6 relative"
                    ref={index === visibleEvents.length - 1 ? lastEventRef : null} // Attach ref to last event
                >
                    {event.type === "vote" ? (
                        <div className="w-1/2 flex justify-end pr-6">
                            <EventCard event={event} />
                        </div>
                    ) : (
                        <div className="w-1/2"></div>
                    )}
                    <div className="w-6 h-6 rounded-full flex items-center justify-center absolute left-1/2 transform -translate-x-1/2 bg-white border-2 border-gray-500">
                        <span className={`${event.type === "vote" ? "bg-custom-cyan" : "bg-custom-red"} w-4 h-4 rounded-full`}></span>
                    </div>
                    {event.type === "bill" ? (
                        <div className="w-1/2 flex justify-start pl-6">
                            <EventCard event={event} />
                        </div>
                    ) : (
                        <div className="w-1/2"></div>
                    )}
                </div>
            ))}
            {events.length > 5 && (
                <div className="text-center mt-4 relative z-10 bg-white p-4">
                    <button
                        onClick={() => setShowAll(!showAll)}
                        className="bg-custom-blue text-white px-4 py-2 rounded-lg shadow-lg"
                    >
                        {showAll ? "Show Less" : "Show More"}
                    </button>
                </div>
            )}
        </div>
    );
};

const EventCard = ({ event }) => {
    return (
        <div className="bg-white shadow-lg rounded-lg p-4 w-64">
            <div className="flex items-center space-x-2">
                <span className={`w-3 h-3 rounded-full ${event.type === "vote" ? "bg-custom-cyan" : "bg-custom-red"}`}></span>
                <span className="text-gray-500 text-sm">{event.date}</span>
            </div>
            <h3 className="font-semibold text-custom-blue mt-2">{event.title}</h3>
            <p className="text-gray-600 text-sm">{event.type === "vote" ? "Vote" : "Bill Topic"}</p>
        </div>
    );
};

export default Timeline;
