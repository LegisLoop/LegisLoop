import { useState } from "react";
import { CompassIcon, HomeIcon, ArrowUpRightCircleIcon } from "../Icons/Icons";
import Tooltip from "../ToolTips/ToolTip";
import { SearchIcon } from "lucide-react";

function ConnectSideBar() {
    const [activeItem, setActiveItem] = useState("Home");

    return (
        <div className="flex flex-col w-full p-4 text-custom-blue">
            <div className="flex items-center gap-2 p-4">
                <CompassIcon />
                <Tooltip
                    text="Use this sidebar to explore posts made by fellow Americans."
                    position="bottom"
                >
                    <h5 className="text-xl font-sans font-semibold leading-snug text-blue-gray-900 cursor-pointer">
                        Connect Page
                    </h5>
                </Tooltip>
            </div>

            <hr className="my-2 border-blue-gray-50" />

            <nav className="flex flex-col gap-1 p-2 text-base text-blue-gray-700">
                {[
                    { icon: <HomeIcon />, label: "Home" },
                    { icon: <ArrowUpRightCircleIcon />, label: "Popular" },
                    { icon: <SearchIcon />, label: "Explore" }
                ].map((item, index) => (
                    <button
                        key={index}
                        onClick={() => setActiveItem(item.label)}
                        className={`flex items-center w-full p-3 transition-all rounded-lg text-start ${
                            activeItem === item.label
                                ? "bg-custom-red-light bg-opacity-50 border-l-4 border-custom-red text-custom-red-dark font-semibold"
                                : "hover:bg-gray-200"
                        }`}
                    >
                        <div
                            className={`grid mr-4 place-items-center ${
                                activeItem === item.label ? "text-custom-red-dark" : "text-custom-blue"
                            }`}
                        >
                            {item.icon}
                        </div>
                        {item.label}
                    </button>
                ))}
            </nav>
        </div>
    );
}

export default ConnectSideBar;
