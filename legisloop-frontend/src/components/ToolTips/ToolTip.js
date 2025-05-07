/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * Tool Tip Declaration.
 ****************************************************************
 * Last Updated: February 19, 2025.
 ***************************************************************/
function Tooltip({ text, children, position = "right" }) {
    const positionClasses = {
        top: "bottom-full mb-2 left-1/2 -translate-x-1/2",
        right: "left-full ml-2 top-1/2 -translate-y-1/2",
        bottom: "top-full mt-2 left-1/2 -translate-x-1/2",
        left: "right-full mr-2 top-1/2 -translate-y-1/2"
    };

    const arrowPosition = {
        top: "left-1/2 -translate-x-1/2 top-full",
        right: "top-1/2 -translate-y-1/2 -left-1.5",
        bottom: "left-1/2 -translate-x-1/2 -top-1.5",
        left: "top-1/2 -translate-y-1/2 -right-1.5"
    };

    return (
        <div className="relative group inline-block">
            {children}
            <div
                className={`absolute w-64 px-3 py-2 text-sm text-white bg-custom-blue rounded-md shadow-lg transition-opacity duration-300 z-50
                    opacity-0 pointer-events-none group-hover:opacity-100 group-hover:pointer-events-auto
                    hidden sm:block
                    ${positionClasses[position]}
                `}
            >
                {text}
                <div
                    className={`absolute w-3 h-3 bg-gray-800 rotate-45 ${arrowPosition[position]}`}
                ></div>
            </div>
        </div>
    );
}
export default Tooltip;
