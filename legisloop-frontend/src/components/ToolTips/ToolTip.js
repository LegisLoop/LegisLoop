/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * Tool Tip Declaration.
 ****************************************************************
 * Last Updated: February 19, 2025.
 ***************************************************************/
function Tooltip({text, children, position = "right" }){
    const positionClasses = {
        top: "bottom-full mb-2 left-1/2 -translate-x-1/2",
        right: "left-full ml-2 top-1/2 -translate-y-1/2",
        bottom: "top-full mt-2 left-1/2 -translate-x-1/2",
        left: "right-full mr-2 top-1/2 -translate-y-1/2"
    };

    return (
        <div className="relative group inline-block">
            {children}
            <div className={`absolute w-64 px-3 py-2 text-sm text-white bg-custom-blue rounded-md opacity-0 transition-opacity delay-300 group-hover:opacity-100 shadow-lg z-50 ${positionClasses[position]}`}>
                {text}
                <div
                    className={`absolute w-3 h-3 bg-gray-800 rotate-45 ${
                        position === "top"
                            ? "left-1/2 -translate-x-1/2 top-full"
                            : position === "right"
                                ? "top-1/2 -translate-y-1/2 -left-1.5"
                                : position === "bottom"
                                    ? "left-1/2 -translate-x-1/2 -top-1.5"
                                    : "top-1/2 -translate-y-1/2 -right-1.5"
                    }`}
                ></div>
            </div>
        </div>
    );
}
export default Tooltip;
