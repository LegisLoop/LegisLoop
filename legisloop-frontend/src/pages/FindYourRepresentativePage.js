import { useState, useEffect } from 'react';
import NavBar from '../components/NavBar/NavBar';
import Footer from '../components/Footer/Footer';
import RepresentativeSideBar from "../components/SideBar/FindYourRepsSideBar";
import { RepresentativeGrid } from "../components/Cards/RepresentativesCard";
import axios from 'axios';

function FindYourRepresentativesPage() {
    const [representatives, setRepresentatives] = useState([]);
    useEffect(() => {
        const fetchRepresentatives = async () => {
            try {
                const response = await axios.get("http://localhost:8080/api/v1/representative/stateId/30");
                console.log('response', response.data);
                setRepresentatives(response.data.slice(0, 20)); // just get the first 10 for now 
            } catch (error) {
                console.error("Error fetching representatives:", error);
            }
        };

        fetchRepresentatives();
    }, []);

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
                    <RepresentativeGrid representatives={representatives} />
                </div>
            </div>

            <Footer className="mt-auto" />
        </div>
    );
}

export default FindYourRepresentativesPage;
