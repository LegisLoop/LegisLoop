/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * Timeline Declaration.
 ****************************************************************
 * Last Updated: February 21, 2025.
 ***************************************************************/
import React, { useState, useLayoutEffect, useRef, useEffect } from "react";
import { DocumentTextIcon } from "../Icons/Icons";
import Tooltip from "../ToolTips/ToolTip";
import TimelineEventCard from "../Cards/TimelineEventCard";
import axios from 'axios';

/*
const events = [
    { date: "MAR 2024", title: "Bill Title", type: "vote" },
    { date: "MAR 2024", title: "Bill Title", type: "sponsoredBill" },
    { date: "JAN 2024", title: "Bill Title", type: "vote" },
    { date: "OCT 2023", title: "Bill Title", type: "sponsoredBill" },
    { date: "JUN 2023", title: "Bill Title", type: "vote" },
    { date: "MAR 2023", title: "Bill Title", type: "sponsoredBill" },
    { date: "MAR 2023", title: "Bill Title", type: "sponsoredBill" },
    { date: "MAR 2023", title: "Bill Title", type: "sponsoredBill" },
    { date: "MAR 2023", title: "Bill Title", type: "sponsoredBill" }
];*/

const Timeline = ({ personID }) => {
    const [showAll, setShowAll] = useState(false);
    const [events, setEvents] = useState([]);

    console.log(personID);

    useEffect(() => {
        const fetchEvents = async () => {
            try {
                const response = await axios.get(`/api/v1/event/${personID}/paginated`);
                console.log('events response', response.data);
                setEvents(response.data.content);
            } catch (error) {
                console.error("Error fetching votes:", error);
            }
        };

        fetchEvents();

    }, [personID]);

    const eventsMap = [
        ...events.map((event) => ({
            date: event.date,
            title: event.event_title,
            position: event.vote_position,
            type: event.type,
            description: event.event_description,
            bill_id: event.bill_id
        }))
    ];

    const visibleEvents = showAll ? eventsMap : eventsMap.slice(0, 5);
    const timelineRef = useRef(null);
    const lastEventRef = useRef(null);
    const [timelineHeight, setTimelineHeight] = useState("5rem");

    useLayoutEffect(() => {
        if (lastEventRef.current && timelineRef.current) {
            const timelineTop = timelineRef.current.getBoundingClientRect().top;
            const lastEventBottom = lastEventRef.current.getBoundingClientRect().bottom;
            setTimelineHeight(`${lastEventBottom - timelineTop}px`);
        }
    }, [visibleEvents, timelineRef, lastEventRef]); // Runs immediately when visibleEvents change

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
                    ref={index === visibleEvents.length - 1 ? lastEventRef : null}
                >
                    {event.type === "VOTE" ? (
                        <div className="w-1/2 flex justify-end pr-6">
                            <TimelineEventCard
                                type={event.type}
                                title={event.title}
                                date={event.date}
                                description={event.description}
                                position={event.position}
                                bill_id={event.bill_id}
                            />
                        </div>
                    ) : (
                        <div className="w-1/2"></div>
                    )}
                    <div className="w-6 h-6 rounded-full flex items-center justify-center absolute left-1/2 transform -translate-x-1/2 bg-white border-2 border-gray-500">
                        <span className={`${event.type === "VOTE" ? "bg-custom-cyan" : "bg-custom-red"} w-4 h-4 rounded-full`}></span>
                    </div>
                    {event.type === "SPONSORED" ? (
                        <div className="w-1/2 flex justify-start pl-6">
                            <TimelineEventCard
                                type={event.type}
                                title={event.title}
                                date={event.date}
                                description={event.description}
                                position={event.position}
                                bill_id={event.bill_id}
                            />
                        </div>
                    ) : (
                        <div className="w-1/2"></div>
                    )}
                </div>
            ))}
            {eventsMap.length > 5 && (
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

export default Timeline;
