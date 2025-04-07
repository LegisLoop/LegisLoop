import NavBar from "../components/NavBar/NavBar";
import Footer from "../components/Footer/Footer";
import LegislationSideBar from "../components/SideBar/LegislationSideBar";
import LegislationVisualizer from "../components/LegislationVisualizer/LegislationVisualizer";
import { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";

// Helper function to convert a Base64 string to a Blob URL.
function createBlobUrl(base64Data, mimeType) {
    if (!base64Data) return "";
    const byteCharacters = atob(base64Data);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], { type: mimeType });
    return URL.createObjectURL(blob);
}

function LegislationPage() {
    // Get the bill id from the route parameter.
    const { id } = useParams();

    const votes = [
        { representative: "John Doe", decision: "Yea" },
        { representative: "Jane Smith", decision: "Nay" },
        { representative: "Alex Johnson", decision: "Abstain" }
    ];

    // Store the legislation data from the API.
    const [legislation, setLegislation] = useState(null);

    useEffect(() => {
        const fetchLegislation = async () => {
            try {
                // Use the bill id from the URL
                const response = await axios.get(`/api/v1/legislation-doc/latest/${id}`);
                console.log("response", response.data);
                setLegislation(response.data);
            } catch (error) {
                console.error("Error fetching bill data:", error);
            }
        };

        if (id) {
            fetchLegislation();
        }
    }, [id]);

    // If no legislation data is available, you can fall back to location.state or defaults.
    const legislationData = legislation || {
        name: "Unknown Representative",
        mime: "application/pdf", // or "text/html" if that's your default
        docContent: "" // Base64 string from backend
    };

    // Convert the Base64 document content to a Blob URL.
    const blobUrl = createBlobUrl(legislationData.docContent, legislationData.mime);

    return (
        <>
            <NavBar />
            <div className="flex min-h-screen">
                <div className="w-[20rem] flex-shrink-0">
                    <LegislationSideBar votes={votes} />
                </div>
                <div className="flex-1 p-6 mx-auto max-w-[900px] lg:mx-2">
                    <LegislationVisualizer mimeType={legislationData.mime} mimeId={blobUrl} />
                </div>
            </div>
            <Footer />
        </>
    );
}

export default LegislationPage;
