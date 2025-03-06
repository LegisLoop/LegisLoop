import NavBar from '../components/NavBar/NavBar';
import Footer from '../components/Footer/Footer';
import RepresentativeSideBar from "../components/SideBar/FindYourRepsSideBar";
import RepresentativeGrid from "../components/Cards/RepresentativesCard";

function FindYourRepresentativesPage() {
    return (
        <div className="flex flex-col">
            <NavBar />

            {/* Main Content: Sidebar + Representative Grid */}
            <div className="flex flex-grow">
                {/* Sidebar (fixed width) */}
                <div className="w-1/4 min-w-[250px] bg-gray-100">
                    <RepresentativeSideBar />
                </div>

                {/* Representative Grid (takes remaining space) */}
                <div className="flex-grow">
                    <RepresentativeGrid />
                </div>
            </div>

            <Footer className="mt-auto" />
        </div>
    );
}

export default FindYourRepresentativesPage;
