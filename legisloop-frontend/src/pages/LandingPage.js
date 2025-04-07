/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * Landing Page Declaration.
 ****************************************************************
 * Last Updated: February 19, 2025.
 ***************************************************************/
import NavBar from "../components/NavBar/NavBar";
import Footer from "../components/Footer/Footer";
import LandingSideBar from "../components/SideBar/LandingSideBar";
import LegislationPreviewCard from "../components/Cards/LegislationPreviewCard";
import EventCard from "../components/Cards/EventCard";
import { CalendarEventIcon } from "../components/Icons/Icons";
import Tooltip from "../components/ToolTips/ToolTip";


function LandingPage() {
    return (
        <>
            <NavBar/>
            <div className="flex min-h-screen">
                <div className="w-15/100 min-w-[250px]">
                    <LandingSideBar/>
                </div>
                <div className="flex-1 w-3/5 p-6 space-y-6">
                    <div className="border-b-2 border-gray-300 pb-4 mb-4">
                        <h2 className="text-2xl font-sans font-bold text-custom-blue">Recent Bills</h2>
                        <p className="text-gray-600 font-sans text-sm">Explore the latest legislative proposals and updates.</p>
                    </div>
                    <div className="overflow-scroll max-h-screen space-y-6">
                    <LegislationPreviewCard
                        category="Economics"
                        title="Bill. Title. 1234"
                        year="Title of 2024"
                        summary="Summary of the bill in one line to get the gist."/>
                    <LegislationPreviewCard
                        category="Economics"
                        title="Bill. Title. 1234"
                        year="Title of 2024"
                        summary="Summary of the bill in one line to get the gist."/>
                    <LegislationPreviewCard
                        category="Economics"
                        title="Bill. Title. 1234"
                        year="Title of 2024"
                        summary="Summary of the bill in one line to get the gist."/>
                        <LegislationPreviewCard
                            category="Economics"
                            title="Bill. Title. 1234"
                            year="Title of 2024"
                            summary="Summary of the bill in one line to get the gist."/>
                        <LegislationPreviewCard
                            category="Economics"
                            title="Bill. Title. 1234"
                            year="Title of 2024"
                            summary="Summary of the bill in one line to get the gist."/>
                        <LegislationPreviewCard
                            category="Economics"
                            title="Bill. Title. 1234"
                            year="Title of 2024"
                            summary="Summary of the bill in one line to get the gist."/>
                        <LegislationPreviewCard
                            category="Economics"
                            title="Bill. Title. 1234"
                            year="Title of 2024"
                            summary="Summary of the bill in one line to get the gist."/>
                        <LegislationPreviewCard
                            category="Economics"
                            title="Bill. Title. 1234"
                            year="Title of 2024"
                            summary="Summary of the bill in one line to get the gist."/>
                        <LegislationPreviewCard
                            category="Economics"
                            title="Bill. Title. 1234"
                            year="Title of 2024"
                            summary="Summary of the bill in one line to get the gist."/>
                        <LegislationPreviewCard
                            category="Economics"
                            title="Bill. Title. 1234"
                            year="Title of 2024"
                            summary="Summary of the bill in one line to get the gist."/>
                        <LegislationPreviewCard
                            category="Economics"
                            title="Bill. Title. 1234"
                            year="Title of 2024"
                            summary="Summary of the bill in one line to get the gist."/>
                        <LegislationPreviewCard
                            category="Economics"
                            title="Bill. Title. 1234"
                            year="Title of 2024"
                            summary="Summary of the bill in one line to get the gist."/>
                    </div>
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
                            signUpLink="/legislation/1804666"
                        />
                        <EventCard
                            title="Event 2"
                            description="Another event happening soon."
                            signUpLink="link to sign up"
                        />
                    </div>
                </div>
            </div>
            <Footer/>
        </>
    );
}
export default LandingPage;
