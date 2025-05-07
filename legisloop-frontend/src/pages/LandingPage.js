/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * LandingPage Declaration.
 ****************************************************************
 * Last Updated: April 3, 2025.
 ***************************************************************/
import NavBar from "../components/NavBar/NavBar";
import Footer from "../components/Footer/Footer";
import LandingSideBar from "../components/SideBar/LandingSideBar";
import LegislationPreviewCard from "../components/Cards/LegislationPreviewCard";
import EventCard from "../components/Cards/EventCard";
import { CalendarEventIcon } from "../components/Icons/Icons";
// import Tooltip from "../components/ToolTips/ToolTip";
import { useState, useEffect, useRef, useLayoutEffect } from "react";
import useGeoLocation from "../customHooks/useGeoLocation";
import useLegislation from "../customHooks/useLegislation";
import Tooltip from "../components/ToolTips/ToolTip";


function LandingPage() {
    const [activeLevel, setActiveLevel] = useState("Federal");
    const [activePolicy, setActivePolicy] = useState(null);
    const [activeStateId, setActiveStateId] = useState(52);
    const [pageNumber, setPageNumber] = useState(0);
    const [locationRequested, setLocationRequested] = useState(false);
    const [searchTerm, setSearchTerm] = useState('');

    const pageSize = 10;

    const { stateId } = useGeoLocation(locationRequested);

    useEffect(() => {
        setPageNumber(0);
    }, [activeLevel, activeStateId]);

    useEffect(() => {
        if (stateId !== null) {
            setActiveStateId(stateId);
        }
    }, [stateId]);

    const { data: bills, loading } = useLegislation(
        activeLevel,
        activeStateId,
        pageNumber,
        pageSize,
        locationRequested,
        searchTerm,
        activePolicy
    );


    const scrollContainerRef = useRef(null);
    const prevScrollHeightRef = useRef(0);

    useLayoutEffect(() => {
        const container = scrollContainerRef.current;
        if (!container) return;

        if (pageNumber > 0) {
            // Calculate the change in height after new content is appended.
            const newScrollHeight = container.scrollHeight;
            const heightDiff = newScrollHeight - prevScrollHeightRef.current;
            // Adjust scrollTop so the user remains at the same position relative to the content.
            container.scrollTop = container.scrollTop + heightDiff;
        }
        // Update the stored scrollHeight.
        prevScrollHeightRef.current = container.scrollHeight;
    }, [bills, pageNumber]);

    const handleScroll = (e) => {
        const { scrollTop, scrollHeight, clientHeight } = e.target;
        if (scrollTop + clientHeight >= scrollHeight - 50 && !loading) {
            setPageNumber((prev) => prev + 1);
        }
    };

    const handleRequestLocation = () => {
        setLocationRequested(true);
    };

    return (
        <div className="flex flex-col min-h-screen">
            <NavBar setSearchTerm={setSearchTerm} setActiveLevel={setActiveLevel} setActivePolicy={setActivePolicy} />
            <div className="flex-1 flex flex-col lg:flex-row items-stretch">
                <LandingSideBar
                    activeLevel={activeLevel}
                    setActiveLevel={setActiveLevel}
                    activePolicy={activePolicy}
                    setActivePolicy={setActivePolicy}
                    setSearchTerm={setSearchTerm}
                    setActiveStateId={setActiveStateId}
                />
                <div className="flex-1 w-auto p-6 space-y-6">
                    <div className="border-b-2 border-gray-300 pb-4 mb-4">
                        <h2 className="text-2xl font-sans font-bold text-custom-blue">
                            Recent Bills
                        </h2>
                        <p className="text-gray-600 font-sans text-sm">
                            Explore the latest legislative proposals and updates.
                        </p>
                    </div>
                    {/* Show the location button only when State-level data is requested and state id is federal (52) */}
                    {activeLevel === "State" && activeStateId === 52 && !locationRequested && (
                        <div className="mb-4">
                            <button
                                className="px-4 py-2 bg-blue-500 text-white rounded"
                                onClick={handleRequestLocation}
                            >
                                Use my location for state legislation
                            </button>
                        </div>
                    )}
                    <div
                        ref={scrollContainerRef}
                        className="overflow-y-scroll max-h-screen space-y-6"
                        onScroll={handleScroll}
                    >
                        {bills.map((bill) => (
                            <LegislationPreviewCard
                                key={bill.bill_id}
                                id={bill.bill_id}
                                category={bill.category}
                                title={bill.title}
                                date={bill.dateIntroduced}
                                summary={bill.summary !== null ? bill.summary : (bill.description !== bill.title ? bill.description : "")}
                                bill={bill}
                            />
                        ))}
                        {loading && <p>Loading...</p>}
                    </div>
                </div>
                <div className="w-full lg:w-[16rem] bg-white p-4 shadow-xl shadow-blue-gray-900/5">
                    <div className="flex items-center gap-4 p-4 mb-2">
                        <CalendarEventIcon />
                        <Tooltip text="View upcoming government meetings, legislative votes, and public policy discussions in your area" position="left">
                            <h5 className="block font-sans text-xl antialiased font-semibold leading-snug tracking-normal text-custom-blue cursor-pointer">
                                Upcoming Events
                            </h5>
                        </Tooltip>
                    </div>
                    <hr className="my-2 border-blue-gray-50" />
                    <div className="space-y-6 mt-6">
                        <EventCard
                            title="Event 1"
                            description="This is a description."
                            signUpLink="link to sign up"
                        />
                        <EventCard
                            title="Event 2"
                            description="Another event happening soon."
                            signUpLink="link to sign up"
                        />
                    </div>
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default LandingPage;