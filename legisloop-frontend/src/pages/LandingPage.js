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

function LandingPage() {
    return (
        <div className="flex flex-col min-h-screen">
            <NavBar />
            <div className="flex-1 flex flex-col lg:flex-row items-stretch">
                <LandingSideBar />
                <div className="flex-1 w-auto p-6 space-y-6">
                    <div className="border-b-2 border-gray-300 pb-4 mb-4">
                        <h2 className="text-2xl font-sans font-bold text-custom-blue">
                            Recent Bills
                        </h2>
                        <p className="text-gray-600 font-sans text-sm">
                            Explore the latest legislative proposals and updates.
                        </p>
                    </div>
                    <div className="space-y-6">
                        <LegislationPreviewCard
                            category="Economics"
                            title="Bill. Title. 1234"
                            year="Title of 2024"
                            summary="Summary of the bill in one line to get the gist."
                        />
                        <LegislationPreviewCard
                            category="Economics"
                            title="Bill. Title. 1234"
                            year="Title of 2024"
                            summary="Summary of the bill in one line to get the gist."
                        />
                    </div>
                </div>
                <div className="w-full lg:w-[16rem] bg-white p-4 shadow-xl shadow-blue-gray-900/5">
                    <div className="flex items-center gap-4 p-4 mb-2">
                        <CalendarEventIcon />
                            <h5 className="block font-sans text-xl antialiased font-semibold leading-snug tracking-normal text-custom-blue cursor-pointer">
                                Upcoming Events
                            </h5>
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
