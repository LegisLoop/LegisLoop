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


const Timeline = ({ personID }) => {

    const [events, setEvents] = useState([]);

    // State for filtering
    const [startDate, setStartDate] = useState("");
    const [endDate, setEndDate] = useState("");
    const [filterActive, setFilterActive] = useState(false);

    // Pagination state.
    const [currentPage, setCurrentPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);

    useEffect(() => {
        if (!filterActive) {
            const fetchPaginatedEvents = async () => {
                try {
                    const response = await axios.get(`/api/v1/event/${personID}/paginated`, {
                        params: { page: currentPage }
                    });
                    const newEvents = response.data.content;
                    
                    // Append events for pages beyond the first.
                    if (currentPage === 0) {
                        setEvents(newEvents);
                    } else {
                        setEvents(prev => [...prev, ...newEvents]);
                    }
                    
                    if (response.data.last) {
                        setHasMore(false);
                    }
                } catch (error) {
                    console.error("Error fetching paginated events:", error);
                }
            };

            fetchPaginatedEvents();
        }
      }, [personID, currentPage, filterActive]);
    
    // Handler for the Filter button (fetches events based on the provided dates)
    const handleFilter = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.get(`/api/v1/event/${personID}/filter`, {
                params: { startDate, endDate },
            });
            // Assuming the filter endpoint returns an array of events.
            setEvents(response.data);
            setFilterActive(true);
            setHasMore(false); // Disable paginated "Show More" when filtering.
        } catch (error) {
            console.error("Error fetching filtered events:", error);
        }
    };
    
    // Handler for clearing the filter and restoring paginated events.
    const handleClearFilter = async () => {
        setStartDate("");
        setEndDate("");
        setFilterActive(false);
        setCurrentPage(0);
        setHasMore(true);
        try {
            const response = await axios.get(`/api/v1/event/${personID}/paginated`, {
                params: { page: 0 }
            });
            setEvents(response.data.content);
            if (response.data.last) {
              setHasMore(false);
            }
        } catch (error) {
            console.error("Error fetching paginated events:", error);
        }
    };

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

    const timelineRef = useRef(null);
    const lastEventRef = useRef(null);
    const [timelineHeight, setTimelineHeight] = useState("5rem");

    useLayoutEffect(() => {
        if (lastEventRef.current && timelineRef.current) {
            const timelineTop = timelineRef.current.getBoundingClientRect().top;
            const lastEventBottom = lastEventRef.current.getBoundingClientRect().bottom;
            setTimelineHeight(`${lastEventBottom - timelineTop}px`);
        }
    }, [events, timelineRef, lastEventRef]); // Runs immediately when events change

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

            {/* Filter Form */}
            <div className="mb-6">
            <form onSubmit={handleFilter} className="flex gap-4 items-end">
                <div>
                <label htmlFor="startDate" className="block mb-1">
                    Start Date:
                </label>
                <input
                    id="startDate"
                    type="date"
                    value={startDate}
                    onChange={(e) => setStartDate(e.target.value)}
                    className="border rounded p-2"
                />
                </div>
                <div>
                <label htmlFor="endDate" className="block mb-1">
                    End Date:
                </label>
                <input
                    id="endDate"
                    type="date"
                    value={endDate}
                    onChange={(e) => setEndDate(e.target.value)}
                    className="border rounded p-2"
                />
                </div>
                <div className="flex flex-col gap-2">
                <button
                    type="submit"
                    className="bg-custom-blue text-white px-4 py-2 rounded-lg shadow-lg"
                >
                    Filter
                </button>
                {filterActive && (
                    <button
                    type="button"
                    onClick={handleClearFilter}
                    className="bg-gray-400 text-white px-4 py-2 rounded-lg shadow-lg"
                    >
                    Clear Filter
                    </button>
                )}
                </div>
            </form>
            </div>


            <hr className="my-2 border-blue-gray-50" />
            <div
                ref={timelineRef}
                className="absolute left-1/2 transform -translate-x-1/2 w-1 bg-gray-300 transition-all duration-500"
                style={{ height: timelineHeight }}
            ></div>

            {eventsMap.map((event, index) => (
                <div
                    key={index}
                    className="flex items-center mb-6 relative"
                    ref={index === eventsMap.length - 1 ? lastEventRef : null}
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
            {!filterActive && hasMore && (
                <div className="text-center mt-4 relative z-10 bg-white p-4">
                    <button
                        onClick={() => setCurrentPage(prev => prev + 1)}
                        className="bg-custom-blue text-white px-4 py-2 rounded-lg shadow-lg"
                    >
                        Show More
                    </button>
                </div>
            )}
        </div>
    );
};

export default Timeline;
