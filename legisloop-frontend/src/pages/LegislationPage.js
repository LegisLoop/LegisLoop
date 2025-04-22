/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * LegislationPage Declaration.
 ****************************************************************
 * Last Updated: April 3, 2025.
 ***************************************************************/
import NavBar from "../components/NavBar/NavBar";
import Footer from "../components/Footer/Footer";
import LegislationSideBar from "../components/SideBar/LegislationSideBar";
import LegislationVisualizer from "../components/LegislationVisualizer/LegislationVisualizer";
import { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";

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
    const { id } = useParams();
    const votes = [
        { representative: "John Doe", decision: "Yea" },
        { representative: "Jane Smith", decision: "Nay" },
        { representative: "Alex Johnson", decision: "Abstain" },
    ];
    const [legislation, setLegislation] = useState(null);

    useEffect(() => {
        if (!id) return;
        axios
            .get(`/api/v1/legislation-doc/latest/${id}`)
            .then(res => setLegislation(res.data))
            .catch(err => console.error("Error fetching bill data:", err));
    }, [id]);

    const data = legislation || {
        name: "Unknown Document",
        mime: "application/pdf",
        docContent: "",
    };
    const blobUrl = createBlobUrl(data.docContent, data.mime);

    return (
        <div className="flex flex-col min-h-screen">
            <NavBar />
            <div className="flex flex-col lg:flex-row flex-1">
                {/* Sidebar: full width on small, 1/3 on lg+ */}
                <div className="w-full lg:w-1/3">
                    <LegislationSideBar votes={votes} />
                </div>
                <div className="w-full lg:w-2/3 p-4 md:p-6 overflow-auto">
                    <div className="mx-auto w-full max-w-full lg:max-w-none">
                        <LegislationVisualizer mimeType={data.mime} mimeId={blobUrl} />
                    </div>
                </div>
            </div>
            <Footer />
        </div>
    );
}

export default LegislationPage;
