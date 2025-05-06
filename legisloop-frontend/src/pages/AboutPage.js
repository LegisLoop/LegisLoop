import NavBar from "../components/NavBar/NavBar";
import LandingSideBar from "../components/SideBar/LandingSideBar";
import LegislationPreviewCard from "../components/Cards/LegislationPreviewCard";
import {CalendarEventIcon} from "../components/Icons/Icons";
import EventCard from "../components/Cards/EventCard";
import Footer from "../components/Footer/Footer";

function AboutPage() {
    return (
        <>
        <div className="flex flex-col min-h-screen">
            <NavBar />
            <div className="max-w-4xl mx-auto bg-white shadow-md p-8 space-y-8">
                <section>
                    <h1 className="text-3xl font-bold text-gray-900 mb-4">About LegisLoop</h1>
                    <p className="text-lg text-gray-700">
                        LegisLoop makes civic engagement simple, transparent, and meaningful.
                    </p>
                    <p className="mt-2 text-gray-700">
                        In a world where political information is overwhelming and social media drives us apart,
                        LegisLoop is your trusted platform for understanding legislation, engaging in productive
                        civic conversations, and taking effective action in our democracy.
                    </p>
                </section>

                <section>
                    <h2 className="text-2xl font-semibold text-gray-900 mb-3">What You Can Do on LegisLoop</h2>
                    <div className="space-y-4">
                        <div>
                            <h3 className="font-medium text-gray-800">Stay Informed, Your Way</h3>
                            <ul className="list-disc list-inside text-gray-700">
                                <li>Personalized Legislative Feeds: Updates on bills and events that matter to you.</li>
                                <li>Accessible Bill Summaries: Adjusted to your preferred reading level.</li>
                                <li>Representative Tracker: See how your officials vote on issues you care about.</li>
                            </ul>
                        </div>
                        <div>
                            <h3 className="font-medium text-gray-800">Join a Balanced Community</h3>
                            <ul className="list-disc list-inside text-gray-700">
                                <li>Cross-Perspective Discussions: Reduce polarization with diverse viewpoints.</li>
                                <li>Local to National: Engage from city councils to Congress.</li>
                                <li>Action-Oriented: Participate in real civic opportunities and events.</li>
                            </ul>
                        </div>
                    </div>
                </section>

                <section>
                    <h2 className="text-2xl font-semibold text-gray-900 mb-3">Who We Are</h2>
                    <p className="text-gray-700">
                        LegisLoop was created by five engineers from Stevens Institute of Technology who believe
                        technology can be a force for good in our democracy.
                    </p>
                    <p className="mt-2 text-gray-700">
                        Frustrated by outdated websites and polarizing media, we built the tool we wished existed—
                        one that brings clarity to legislation and fosters civil discourse.
                    </p>
                    <div className="mt-4">
                        <h3 className="font-medium text-gray-800">Our founding team:</h3>
                        <ul className="list-disc list-inside text-gray-700">
                            <li>Anthony Ford</li>
                            <li>Damien McDevitt</li>
                            <li>Stephanie McDonough</li>
                            <li>Jonathan Memoli</li>
                            <li>Sean Tu</li>
                        </ul>
                    </div>
                    <p className="mt-4 text-gray-700">
                        We're building LegisLoop because we believe that better civic tools empower citizens and
                        lead to a more responsive, healthy democracy.
                    </p>
                </section>
            </div>
        <div>
            <Footer />
        </div>
        </div>
        </>
    );
}

export default AboutPage;