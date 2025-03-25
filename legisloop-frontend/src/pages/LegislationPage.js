import NavBar from "../components/NavBar/NavBar";
import Footer from "../components/Footer/Footer";
import LegislationSideBar from "../components/SideBar/LegislationSideBar";
import {CalendarEventIcon} from "../components/Icons/Icons";
import Tooltip from "../components/ToolTips/ToolTip";
import EventCard from "../components/Cards/EventCard";

function LegislationPage() {
    // Hardcoded vote data
    const votes = [
        { representative: "John Doe", decision: "Yea" },
        { representative: "Jane Smith", decision: "Nay" },
        { representative: "Alex Johnson", decision: "Abstain" }
    ];

    return (
        <>
            <NavBar />
            <div className="flex min-h-screen">
            <div className="flex">
                {/* Passing hardcoded votes to the sidebar */}
                <LegislationSideBar votes={votes} />
                {/* Main Content (Legislation details) */}
                <div className="flex-1 p-4">Legislation Content Here</div>
            </div>
            <div className="overflow-auto w-full max-h-screen-xl max-w-[20rem] bg-white p-4 shadow-xl shadow-blue-gray-900/5">
                <div className="flex items-center gap-4 p-4 mb-2">
                    <CalendarEventIcon/>
                    <Tooltip text="View upcoming government meetings, legislative votes, and public policy discussions in your area" position="bottom">
                        <h5 className="block font-sans text-xl antialiased font-semibold leading-snug tracking-normal text-custom-blue cursor-pointer">
                            Upcoming Events
                        </h5>
                    </Tooltip>
                </div>
                <hr className="my-2 border-blue-gray-50"/>
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

export default LegislationPage;
