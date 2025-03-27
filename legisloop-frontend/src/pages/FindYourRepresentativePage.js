import { useState, useEffect } from 'react';
import NavBar from '../components/NavBar/NavBar';
import Footer from '../components/Footer/Footer';
import RepresentativeSideBar from "../components/SideBar/FindYourRepsSideBar";
import { RepresentativeGrid } from "../components/Cards/RepresentativesCard";
import axios from 'axios';

function FindYourRepresentativesPage() {
    const [representatives, setRepresentatives] = useState([]);
    const [stateId, setStateId] = useState();

    useEffect(() => {
        const fetchRepresentatives = async () => {
            try {
                const response = await axios.get(`http://localhost:8080/api/v1/representative/stateId/${stateId}`);
                console.log('response', response.data);
                setRepresentatives(response.data.slice(0, 20)); // just get the first 20 for now 
            } catch (error) {
                console.error("Error fetching representatives:", error);
            }
        };

        fetchRepresentatives();
    }, [stateId]);

    return (
        <div className="flex flex-col">
            <NavBar />

            {/* Main Content: Sidebar + Representative Grid */}
            <div className="flex flex-grow">
                {/* Sidebar (fixed width) */}
                <div className="w-1/4 min-w-[250px] bg-gray-100">
                    <RepresentativeSideBar setStateId={setStateId} />
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
