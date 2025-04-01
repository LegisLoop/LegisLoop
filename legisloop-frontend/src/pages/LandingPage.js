import NavBar from "../components/NavBar/NavBar";
import Footer from "../components/Footer/Footer";
import LegislationSideBar from "../components/SideBar/LegislationSideBar";
import LegislationPreviewCard from "../components/Cards/LegislationPreviewCard";
import EventCard from "../components/Cards/EventCard";
import { CalendarEventIcon } from "../components/Icons/Icons";
import Tooltip from "../components/ToolTips/ToolTip";
import { useState, useEffect, useRef, useLayoutEffect } from "react";
import useLegislation from "../Hooks/useLegislation";
import useGeoLocation from "../Hooks/useGeoLocation";

function LandingPage() {
    const [activeLevel, setActiveLevel] = useState("Federal");
    const [activePolicy, setActivePolicy] = useState(null);
    const [activeStateId, setActiveStateId] = useState(10);
    const [pageNumber, setPageNumber] = useState(0);

    useEffect(() => {
        setPageNumber(0);
    }, [activeLevel, activeStateId]);

    const { data: bills, loading, error } = useLegislation(
        activeLevel,
        activeStateId,
        pageNumber
    );

    const { stateId, error: locationError } = useGeoLocation();
    useEffect(() => {
        if (stateId !== null) {
            setActiveStateId(stateId);
        }
    }, [stateId]);

    // Ref for the scroll container.
    const scrollContainerRef = useRef(null);
    // Ref to store the previous scrollHeight.
    const prevScrollHeightRef = useRef(0);

    // When new bills are loaded, adjust the scroll position based on the change in scroll height.
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

    return (
        <>
            <NavBar />
            <div className="flex min-h-screen">
                <div className="w-15/100 min-w-[250px]">
                    <LegislationSideBar
                        activeLevel={activeLevel}
                        setActiveLevel={setActiveLevel}
                        activePolicy={activePolicy}
                        setActivePolicy={setActivePolicy}
                    />
                </div>
                <div className="flex-1 w-3/5 p-6 space-y-6">
                    <div className="border-b-2 border-gray-300 pb-4 mb-4">
                        <h2 className="text-2xl font-sans font-bold text-custom-blue">
                            Recent Bills
                        </h2>
                        <p className="text-gray-600 font-sans text-sm">
                            Explore the latest legislative proposals and updates.
                        </p>
                    </div>
                    <div
                        ref={scrollContainerRef}
                        className="overflow-scroll max-h-screen space-y-6"
                        onScroll={handleScroll}
                    >
                        {loading && <p>Loading...</p>}
                        {error && <p>Error Fetching Data: {error.message}</p>}
                        {!loading &&
                            !error &&
                            bills.map((bill) => (
                                <LegislationPreviewCard
                                    key={bill.id}
                                    category={bill.category}
                                    title={bill.title}
                                    year={bill.year}
                                    summary={bill.description}
                                />
                            ))}
                    </div>
                </div>
                <div className="overflow-auto w-full max-h-screen-xl max-w-[20rem] bg-white p-4 shadow-xl shadow-blue-gray-900/5">
                    <div className="flex items-center gap-4 p-4 mb-2">
                        <CalendarEventIcon />
                        <Tooltip
                            text="View upcoming government meetings, legislative votes, and public policy discussions in your area"
                            position="bottom"
                        >
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
        </>
    );
}

export default LandingPage;
