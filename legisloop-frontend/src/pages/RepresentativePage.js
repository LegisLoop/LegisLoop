/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * Single Representative Page Declaration.
 ****************************************************************
 * Last Updated: February 21, 2025.
 ***************************************************************/

import NavBar from '../components/NavBar/NavBar';
import Footer from '../components/Footer/Footer';
import Timeline from '../components/Timeline/Timeline';
import ProfileCard from "../components/Cards/ProfileCard";
import StateSelectorBar from "../components/StateSelector/StateSelectorBar";

function RepresentativePage() {
    return (
        <div>
            <NavBar />
            <StateSelectorBar/>
            <main className="flex flex-col  py-10">
                <div className="container mx-auto px-4">
                    <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 items-start">
                        <div className="lg:col-span-1 flex justify-center">
                            <ProfileCard className="w-full max-w-md shadow-lg border border-gray-300 rounded-lg bg-white p-6"
                            name="Name of Representative" position="Title of Position" phoneNumber="Phone Number" email="Contact Email" serviceLength="Service Length" topics="Key Topics of Interest"/>
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
