/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * LegislationPage Declaration.
 ****************************************************************
 * Last Updated: April 3, 2025.
 ******************************/
import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";

import NavBar from "../components/NavBar/NavBar";
import Footer from "../components/Footer/Footer";
import LegislationSideBar from "../components/SideBar/LegislationSideBar";
import LegislationVisualizer from "../components/LegislationVisualizer/LegislationVisualizer";
import useLegislationSummary from "../customHooks/useLegislationSummary";

// Helper to turn Base64 → Blob URL
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

export default function LegislationPage() {
    const { id } = useParams();
    const [legislation, setLegislation] = useState(null);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");

    // reading-level state drives both the sidebar highlight AND our summary hook
    const [activeLevel, setActiveLevel] = useState("UN_EDITED");

    // 1) load the raw legislation doc
    useEffect(() => {
        if (!id) return;

        const fetchDoc = async () => {
            setLoading(true);
            setErrorMessage("");
            try {
                const resp = await axios.get(`/api/v1/legislation-doc/latest/${id}`);
                if (resp.status === 204) {
                    setErrorMessage("No content for this legislation, yet.");
                } else {
                    setLegislation(resp.data);
                }
            } catch (err) {
                if (err.response?.status === 404) {
                    setErrorMessage("Bill not found.");
                } else {
                    setErrorMessage("An error occurred while fetching legislation.");
                }
            } finally {
                setLoading(false);
            }
        };

        fetchDoc();
    }, [id]);

    // 2) call our summary hook (only runs once doc is loaded)
    const {
        summary,
        loading: summaryLoading,
        error: summaryError
    } = useLegislationSummary(
        legislation?.docId ?? id,       // your docId field
        activeLevel,
        legislation?.docContent || "",
        legislation?.mime || ""
    );

    // 3) decide what to render
    let content;
    if (loading) {
        content = <p className="text-center text-blue-500">Fetching data…</p>;
    } else if (errorMessage) {
        content = <p className="text-center text-red-500">{errorMessage}</p>;
    } else if (activeLevel === "UN_EDITED") {
        // raw doc view
        const blobUrl = createBlobUrl(legislation.docContent, legislation.mime);
        content = (
            <LegislationVisualizer mimeType={legislation.mime} mimeId={blobUrl} />
        );
    } else if (summaryLoading) {
        content = <p className="text-center">Loading summary…</p>;
    } else if (summaryError) {
        content = (
            <p className="text-center text-red-500">
                Error loading summary. Please try again.
            </p>
        );
    } else {
        // summary view
        content = (
            <div className="prose max-w-none p-4">
                <div>{summary.summaryContent}</div>
            </div>
        );
    }

    return (
        <div className="flex flex-col min-h-screen">
            <NavBar />
            <div className="flex flex-1 flex-col lg:flex-row">
                <div className="w-full lg:w-1/3">
                    <LegislationSideBar
                        votes={legislation?.votes || []}
                        activeLevel={activeLevel}
                        setActiveLevel={setActiveLevel}
                    />
                </div>
                <div className="w-full lg:w-2/3 overflow-auto p-4 md:p-6">
                    {content}
                </div>
            </div>
            <Footer />
        </div>
    );
}

