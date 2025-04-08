/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * NavigationBar Declaration.
 ****************************************************************
 * Last Updated: February 18, 2025.
 ***************************************************************/
import { Link, useLocation } from "react-router-dom";
import SearchBar from "../SearchBar/SearchBar";
import {LegisLoopLogo} from "../Icons/Icons";

function NavBar() {
    const location = useLocation();

    const navItems = [
        { path: "/", label: "LegisLoop"},
        { path: "/legislation", label: "Legislation" },
        { path: "/findrepresentatives", label: "Find Your Representatives" },
        { path: "/connect", label: "Connect" }
    ];

    return (
        <nav className="flex items-center justify-between font-sans bg-custom-blue text-[#fff6ec] h-16 sticky top-0 left-0 right-0 z-10 px-6 shadow-md shadow-blue-gray-900/5">
            <div className="flex items-center justify-center">
                <Link to="/" className="flex items-center space-x-2">
                    <LegisLoopLogo className="size-11 mr-6"/>
                </Link>
                <ul className="hidden md:flex space-x-6">
                    {navItems.slice(0, 3).map((item) => (
                        <li key={item.path} className="relative">
                            <Link
                                to={item.path}
                                className={`relative flex h-full px-4 py-2 transition-all duration-300 before:content-[''] before:absolute before:w-full before:h-[2px] before:bottom-0 before:left-0 before:bg-[#fff6ec] before:scale-x-0 before:transition-transform before:duration-300 before:ease-in-out hover:before:scale-x-100 ${
                                    location.pathname === item.path
                                        ? "font-bold before:scale-x-100"
                                        : "hover:text-[#fff6ec]"
                                }`}
                            >
                                {item.label}
                            </Link>
                        </li>
                    ))}
                </ul>
            </div>
            <div className="hidden md:flex items-center space-x-6">
                <SearchBar />
                <ul className="flex space-x-6 items-center justify-center">
                    <li className="relative">
                        <Link
                            to="/connect"
                            className={`relative flex h-full px-4 py-2 transition-all duration-300 before:content-[''] before:absolute before:w-full before:h-[2px] before:bottom-0 before:left-0 before:bg-[#fff6ec] before:scale-x-0 before:transition-transform before:duration-300 before:ease-in-out hover:before:scale-x-100 ${
                                location.pathname === "/connect"
                                    ? "font-bold before:scale-x-100"
                                    : "hover:text-[#fff6ec]"
                            }`}
                        >
                            Connect
                        </Link>
                    </li>
                </ul>
            </div>
        </nav>
    );
}

export default NavBar;
