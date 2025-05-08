/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * Legislation Preview Card Declaration.
 ****************************************************************
 * Last Updated: February 19, 2025.
 ***************************************************************/

import { Link } from "react-router-dom";

function LegislationPreviewCard({ id, category, title, date, summary, bill, documents, sponsors }) {
    return (
        <div className="w-full md:max-w-screen-lg bg-gray-50 p-4 rounded-lg shadow-sm border border-gray-200 relative">
            <div className="flex items-center gap-2 text-gray-600 text-sm font-semibold uppercase">
                <div className="w-2 h-5 bg-teal-500 rounded"></div>
                <span>{category ? category : ""}{category && date ? " : " : ""}{date ? date : ""}</span>
            </div>
            <div className="absolute top-2 right-4">
                {documents && documents.length > 0 ? (
                    <span className="text-green-600 font-bold text-base">Available</span>
                ) : (
                    <span className="text-red-500 font-bold text-base">No Bill Text Available</span>
                )}
            </div>
            <h2 className="text-lg font-bold text-gray-900 mt-1">
                <Link
                    to={`/legislation/${id}`}
                    state={{ bill }}
                >
                    {title}
                </Link>
            </h2>
            <p className="text-gray-500 text-sm mt-1">{summary}</p>
            {/* Sponsors */}
            {sponsors && sponsors.length > 0 && (
                <div className="mt-3 flex flex-wrap gap-x-3 gap-y-1 items-center">
                    <span className="text-yellow-900 font-bold text-sm">Sponsored by:</span>
                    {sponsors.map((sponsor, idx) => (
                        <Link
                            key={sponsor.people_id || idx}
                            to={`/representative/${sponsor.people_id}`}
                            state={{
                                id: sponsor.people_id,
                                name: sponsor.name,
                                title: sponsor.role,
                                service: `District ${sponsor.district}`,
                                topics: sponsor.party
                            }}
                            className="text-yellow-900 font-bold text-sm underline hover:text-yellow-700"
                        >
                            {sponsor.name} ({sponsor.party})
                        </Link>
                    ))}
                </div>
            )}
        </div>
    );
}

export default LegislationPreviewCard;
