/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * Connect Page Declaration.
 ****************************************************************
 * Last Updated: February 19, 2025.
 ***************************************************************/
import NavBar from "../components/NavBar/NavBar";
import Footer from "../components/Footer/Footer";
import PostCard from "../components/Cards/PostCard";
import ConnectSideBar from "../components/SideBar/ConnectSideBar";

function ConnectPage() {
    return (
        <>
            <NavBar />
            <div className="flex flex-col md:flex-row">
                {/* Sticky Sidebar */}
                <div className="w-15/100 min-w-[250px]">
                    <ConnectSideBar/>
                </div>

                {/* Post Section */}
                <div className="w-full md:w-3/4 lg:w-4/5 p-2 space-y-4">
                    <PostCard
                        username="username"
                        date="mm-dd-yyyy"
                        title="Title of Post"
                        body="This is the body of the post. People can express their opinions and things"/>
                    <PostCard />
                    <PostCard />
                    <PostCard />
                </div>
            </div>
            <Footer />
        </>
    );
}

export default ConnectPage;
