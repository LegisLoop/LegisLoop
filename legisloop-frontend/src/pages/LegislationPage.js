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
import { useParams, useLocation } from "react-router-dom";

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
    const { state } = useLocation();
    const initialBill = state?.bill;

    // --- Bill & document state ---
    const [bill, setBill] = useState(initialBill || null);
    const [doc, setDoc] = useState(null);
    const [loading, setLoading] = useState(true);
    const [errorMessage, setErrorMessage] = useState("");

    // --- Derived state for votes & summary ---
    const [latestRoll, setLatestRoll] = useState(null);
    const [votes, setVotes] = useState([]);
    const [rollCallSummary, setRollCallSummary] = useState({
        yea: 0,
        nay: 0,
        abstain: 0,
    });

    // load bill info if not provided from landing page 
    useEffect(() => {
        if (initialBill) {
            setBill(initialBill);
        } else {
            axios
                .get(`/api/v1/legislation/${id}`)
                .then((res) => setBill(res.data))
                .catch((e) => {
                    console.error(e);
                    setErrorMessage("Failed to load bill details.");
                });
        }
    }, [id, initialBill]);

    // fetch the document 
    useEffect(() => {
        if (!id) return;

        setLoading(true);
        axios
            .get(`/api/v1/legislation-doc/latest/${id}`)
            .then((res) => {
                if (res.status === 204) {
                    setErrorMessage("No content for this legislation, yet.");
                } else {
                    setDoc(res.data);
                }
            })
            .catch((e) => {
                if (e.response?.status === 404) {
                    setErrorMessage("Bill not found.");
                } else {
                    setErrorMessage("An error occurred while fetching document.");
                }
            })
            .finally(() => setLoading(false));
    }, [id]);

    // recalculate latestRoll, votes & summary
    useEffect(() => {
        if (!bill?.roll_calls?.length) {
            setLatestRoll(null);
            setVotes([]);
            setRollCallSummary({ yea: 0, nay: 0, abstain: 0 });
            return;
        }

        const roll = bill.roll_calls[0];
        setLatestRoll(roll);

        const mappedVotes = roll.votes.map((v) => ({
            person_id: v.person_id,
            decision: v.vote_position,
        }));
        setVotes(mappedVotes);

        setRollCallSummary({
            yea: roll.yea,
            nay: roll.nay,
            abstain: (roll.nv || 0) + (roll.absent || 0),
        });
    }, [bill]);

    return (
        <div className="flex flex-col min-h-screen">
            <NavBar />

            <div className="flex flex-col lg:flex-row flex-1">
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
                    />
                </div>

                {/* Main content */}
                <div className="w-full lg:w-2/3 p-4 md:p-6 overflow-auto">
                    <div className="mx-auto w-full max-w-full lg:max-w-none">
                        {bill && (
                            <div className="mb-6 border-b pb-4">
                                <h1 className="text-2xl font-bold">{bill.title}</h1>
                                <div className="mt-3 prose">
                                    {bill.title !== bill.description && (
                                        <p>
                                            <strong>Description:</strong> {bill.description}
                                        </p>
                                    )}
                                    <p>
                                        <strong>Sponsors:</strong>{" "}
                                        {bill.sponsors.map((s) => s.name).join(", ")}
                                    </p>
                                </div>
                            </div>
                        )}

                        {loading ? (
                            <p className="text-center text-blue-500">
                                Fetching document…
                            </p>
                        ) : errorMessage ? (
                            <p className="text-center text-red-500">
                                {errorMessage}
                            </p>
                        ) : (
                            <LegislationVisualizer
                                mimeType={doc.mime}
                                mimeId={createBlobUrl(doc.docContent, doc.mime)}
                            />
                        )}
                    </div>
                </div>
            </div>

            <Footer />
        </div>
    );
}

export default LegislationPage;

