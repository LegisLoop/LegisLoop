import { useNavigate, useLocation } from "react-router-dom";
import { ArrowLeft } from "lucide-react";
import NavBar from '../components/NavBar/NavBar';
import Footer from '../components/Footer/Footer';
import Timeline from '../components/Timeline/Timeline';
import ProfileCard from "../components/Cards/ProfileCard";

function RepresentativePage() {
    const navigate = useNavigate();
    const location = useLocation();

    // Extract state from navigation
    const repData = location.state || {
        name: "Unknown Representative",
        title: "Unknown Position",
        service: "Unknown Service Length",
        topics: "No topics available",
        phoneNumber: "N/A",
        email: "N/A",
    };

    return (
        <div>
            <NavBar />

            {/* Back Button */}
            <div className="container mx-auto px-4 mt-4">
                <button
                    onClick={() => navigate(-1)}
                    className="flex items-center text-blue-600 hover:underline mb-4"
                >
                    <ArrowLeft className="mr-2" size={20} />
                    Back to All Representatives
                </button>
            </div>

            <main className="flex flex-col">
                <div className="container mx-auto px-4">
                    <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 items-start">
                        <div className="lg:col-span-1 flex justify-center">
                            <ProfileCard
                                className="w-full max-w-md shadow-lg border border-gray-300 rounded-lg bg-white p-6"
                                name={repData.name}
                                position={repData.title}
                                phoneNumber={repData.phoneNumber}
                                email={repData.email}
                                serviceLength={repData.service}
                                topics={repData.topics}
                            />
                        </div>
                        <div className="lg:col-span-2 flex justify-center">
                            <div className="w-full max-w-3xl bg-white shadow-lg border border-gray-300 rounded-lg p-6">
                                <Timeline />
                            </div>
                        </div>
                    </div>
                </div>
            </main>

            <Footer className="mt-auto" />
        </div>
    );
}

export default RepresentativePage;
