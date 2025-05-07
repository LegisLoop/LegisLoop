/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * Connect Page Declaration.
 ****************************************************************
 * Last Updated: April 3, 2025.
 ***************************************************************/
import NavBar from "../components/NavBar/NavBar";
import Footer from "../components/Footer/Footer";
import PostCard from "../components/Cards/PostCard";
import ConnectSideBar from "../components/SideBar/ConnectSideBar";

function ConnectPage() {
    return (
        <div className="flex flex-col min-h-screen overflow-x-hidden">
            <NavBar />
            <div className="flex-1 flex flex-col md:flex-row items-stretch">
                <div className="w-full md:w-1/4 min-w-[250px] bg-white shadow-xl shadow-blue-gray-900/5 flex flex-col">
                    <ConnectSideBar />
                </div>
                <div className="flex-1 p-2 space-y-4">
                    <PostCard
                        username="healthforall"
                        date="04-07-2025"
                        title="Affordable Healthcare: Finding Common Ground"
                        body="Most Americans agree that healthcare should be accessible and efficient. What are practical ways to improve the system without extreme overhauls?"
                    />
                    <PostCard
                        username="civicvoice"
                        date="04-08-2025"
                        title="Balancing Public Health and Personal Freedom"
                        body="Policies on public health should protect the community while respecting individual freedoms. Collaboration, not conflit, will move us forward. What safeguards should exist to keep both in check?"
                    />
                    <PostCard
                        username="greenmind23"
                        date="04-10-2025"
                        title="Investing in Renewable Energy: A Step Toward Sustainability"
                        body="Renewable energy creates jobs and reduces long-term costs. Strategic government investment today can lead to energy independence tomorrow. Thoughts on balancing innovation with fiscal responsibility?"
                    />
                    <PostCard
                        username="libertyfocus"
                        date="04-07-2025"
                        title="Maintaining National Security and Civil Liberties"
                        body="Security is important, but so are our constitutional rights. How do we create policies that respect both without leaning too far in either direction?"
                    />
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default ConnectPage;
