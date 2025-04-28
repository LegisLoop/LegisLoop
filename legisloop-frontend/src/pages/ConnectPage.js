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
                        username="username"
                        date="mm-dd-yyyy"
                        title="Title of Post"
                        body="This is the body of the post. People can express their opinions and things."
                    />
                    <PostCard />
                    <PostCard />
                    <PostCard />
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default ConnectPage;
