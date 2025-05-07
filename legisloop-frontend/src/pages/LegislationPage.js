/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * LegislationPage Declaration.
 ****************************************************************
 * Last Updated: May 6, 2025.
 ***************************************************************/
import React, { useState, useEffect } from "react";
import {
    useParams,
    useLocation,
} from "react-router-dom";
import axios from "axios";

import NavBar from "../components/NavBar/NavBar";
import Footer from "../components/Footer/Footer";
import LegislationSideBar from "../components/SideBar/LegislationSideBar";
import LegislationVisualizer from "../components/LegislationVisualizer/LegislationVisualizer";
import useLegislationSummary from "../customHooks/useLegislationSummary";

// Helper to turn Base64 → Blob URL
function createBlobUrl(base64Data, mimeType) {
    if (!base64Data || !mimeType) return "";
    const byteCharacters = atob(base64Data);
    const byteNumbers = new Uint8Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    return URL.createObjectURL(new Blob([byteNumbers], { type: mimeType }));
}

export default function LegislationPage() {
    const { id } = useParams();
    const { state } = useLocation();
    const initialBill = state?.bill;

    // — Bill metadata
    const [bill, setBill] = useState(initialBill || null);

    // — Document fetch state
    const [doc, setDoc] = useState(null);
    const [loadingDoc, setLoadingDoc] = useState(true);
    const [errorDoc, setErrorDoc] = useState("");

    // — Reading-level state
    const [activeLevel, setActiveLevel] = useState("UN_EDITED");

    // — Votes & roll-call summary
    const [votes, setVotes] = useState([]);
    const [rollCallSummary, setRollCallSummary] = useState({
        yea: 0,
        nay: 0,
        abstain: 0,
    });

    // fetch bill metadata if not passed via location.state
    useEffect(() => {
        if (initialBill) {
            setBill(initialBill);
        } else {
            axios
                .get(`/api/v1/legislation/${id}`)
                .then((res) => setBill(res.data))
                .catch((e) => {
                    console.error("Failed to load bill details", e);
                });
        }
    }, [id, initialBill]);

    // fetch the latest document
    useEffect(() => {
        if (!id) return;

        setLoadingDoc(true);
        setErrorDoc("");
        axios
            .get(`/api/v1/legislation-doc/latest/${id}`)
            .then((res) => {
                if (res.status === 204) {
                    setErrorDoc("No content for this legislation, yet.");
                } else {
                    setDoc(res.data);
                }
            })
            .catch((e) => {
                if (e.response?.status === 404) {
                    setErrorDoc("Document not found.");
                } else {
                    setErrorDoc("An error occurred while fetching document.");
                }
            })
            .finally(() => setLoadingDoc(false));
    }, [id]);

    // votes & roll-call summary whenever the bill loads
    useEffect(() => {
        if (bill?.roll_calls?.length) {
            const roll = bill.roll_calls[0];
            setVotes(
                roll.votes.map((v) => ({
                    person_id: v.person_id,
                    decision: v.vote_position,
                }))
            );
            setRollCallSummary({
                yea: roll.yea,
                nay: roll.nay,
                abstain: (roll.nv || 0) + (roll.absent || 0),
            });
        } else {
            setVotes([]);
            setRollCallSummary({ yea: 0, nay: 0, abstain: 0 });
        }
    }, [bill]);

    const {
        summary,
        loading: loadingSummary,
        error: errorSummary,
    } = useLegislationSummary(
        doc?.docId,
        activeLevel,
        doc?.docContent || "",
        doc?.mime || ""
    );

    let content;

    if (loadingDoc) {
        content = <p className="text-center text-blue-500">Fetching document…</p>;
    } else if (errorDoc) {
        content = <p className="text-center text-red-500">{errorDoc}</p>;
    } else if (activeLevel === "UN_EDITED") {
        const blobUrl = createBlobUrl(doc.docContent, doc.mime);
        content = <LegislationVisualizer mimeType={doc.mime} mimeId={blobUrl} />;
    } else if (loadingSummary) {
        content = <p className="text-center">Loading summary…</p>;
    } else if (errorSummary) {
        content = (
            <p className="text-center text-red-500">
                {errorSummary.message || "Error loading summary."}
            </p>
        );
    } else if (summary && summary.summaryContent) {
        content = (
            <div className="prose max-w-none p-4">
                {summary.summaryContent}
            </div>
        );
    } else {
        content = (
            <p className="text-center text-gray-500">
                No summary available for this reading level.
            </p>
        );
    }

    return (
        <div className="flex flex-col min-h-screen">
            <NavBar />

            <div className="flex flex-1 flex-col lg:flex-row">
                {/* Sidebar */}
                <div className="w-full lg:w-1/3">
                    <LegislationSideBar
                        votes={votes}
                        rollCallSummary={rollCallSummary}
                        billInfo={{
                            status: bill?.status,
                            dateIntroduced: bill?.dateIntroduced,
                            sponsors: bill?.sponsors,
                        }}
                        activeLevel={activeLevel}
                        setActiveLevel={setActiveLevel}
                    />
                </div>

                <div className="w-full lg:flex-1 overflow-auto p-4 md:p-6">
                    {bill && (
                        <div className="mb-6 border-b pb-4">
                            <h1 className="text-2xl font-bold">{bill.title}</h1>

                            <div className="mt-3 prose">
                                <p>
                                    <strong>Introduced:</strong>{" "}
                                    {new Date(bill.dateIntroduced).toLocaleDateString()}
                                </p>

                                {bill.description && (
                                    <p>
                                        <strong>Description:</strong> {bill.description}
                                    </p>
                                )}

                                {bill.sponsors?.length > 0 && (
                                    <p>
                                        <strong>Sponsors:</strong>{" "}
                                        {bill.sponsors.map((s, i) => (
                                            <span key={s.people_id}>
                                                {i > 0 && ", "}
                                                {s.name}
                                            </span>
                                        ))}
                                    </p>
                                )}
                            </div>
                        </div>
                    )}
                    {content}
                </div>
            </div>

            <Footer />
        </div>
    );
}