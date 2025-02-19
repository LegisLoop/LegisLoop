/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * Legislation Preview Card Declaration.
 ****************************************************************
 * Last Updated: February 19, 2025.
 ***************************************************************/
function LegislationPreviewCard({category, title, year, summary}) {
    return (
        <div className="bg-gray-50 p-4 rounded-lg shadow-sm border border-gray-200 max-w-screen-lg">
            <div className="flex items-center gap-2 text-gray-600 text-sm font-semibold uppercase">
                <div className="w-2 h-5 bg-teal-500 rounded"></div>
                <span>{category}</span>
            </div>
            <h2 className="text-lg font-bold text-gray-900 mt-1">
                {title} <span className="uppercase">{year}</span>
            </h2>
            <p className="text-gray-500 text-sm mt-1">
                {summary}
            </p>
        </div>
    );
}

export default LegislationPreviewCard;
