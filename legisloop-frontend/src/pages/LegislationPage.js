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
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        const fetchLegislation = async () => {
            try {
                // Use the bill id from the URL
                const response = await axios.get(`/api/v1/legislation-doc/latest/${id}`);
                
                // Check for HTTP 204 (no content)
                if (response.status === 204) {
                    setErrorMessage("No content for this legislation, yet.");
                } else {
                    setLegislation(response.data);
                }
            } catch (error) {
                // If the error response is available, check for 404
                if (error.response && error.response.status === 404) {
                    setErrorMessage("Bill not found.");
                } else {
                    setErrorMessage("An error occurred while fetching data.");
                }
            } finally {
                setLoading(false);
            }
        };

        if (id) {
            fetchLegislation();
        }
    }, [id]);

    // Determine what to render in the content area.
    let content;
    if (loading) {
        content = <p className="text-center text-blue-500">Fetching data...</p>;
    } else if (errorMessage) {
        content = <p className="text-center text-red-500">{errorMessage}</p>;
    } else {
        // Use the fetched legislation data; fall back to default values if for some reason data is absent.
        const legislationData = legislation || {
            name: "Unknown Representative",
            mime: "application/pdf",
            docContent: ""
        };

        // Convert the Base64 document content to a Blob URL.
        const blobUrl = createBlobUrl(legislationData.docContent, legislationData.mime);

        content = <LegislationVisualizer mimeType={legislationData.mime} mimeId={blobUrl} />;
    }

    return (
        <>
            <NavBar />
            <div className="flex min-h-screen">
                <div className="w-[20rem] flex-shrink-0">
                    <LegislationSideBar votes={votes} />
                </div>
                <div className="flex-1 p-6 mx-auto max-w-[900px] lg:mx-2">
                    {content}
                </div>
            </div>
            <Footer />
        </>
    );
}

export default LegislationPage;
