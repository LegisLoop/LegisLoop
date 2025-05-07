/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * Legislation Preview Card Declaration.
 ****************************************************************
 * Last Updated: February 19, 2025.
 ***************************************************************/

import { Link } from "react-router-dom";

function LegislationPreviewCard({ id, category, title, date, summary, bill }) {
    return (
        <div className="w-full md:max-w-screen-lg bg-gray-50 p-4 rounded-lg shadow-sm border border-gray-200">
            <div className="flex items-center gap-2 text-gray-600 text-sm font-semibold uppercase">
                <div className="w-2 h-5 bg-teal-500 rounded"></div>
                <span>{category ? category : ""}{category && date ? " : " : ""}{date ? date : ""}</span>
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
        </div>
    );
}

export default LegislationPreviewCard;
