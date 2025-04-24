/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * NavigationBar Declaration (Hamburger at lg).
 ****************************************************************
 * Last Updated: April 3, 2025.
 ***************************************************************/
import React, { useState } from "react";
import { Link, useLocation } from "react-router-dom";
import SearchBar from "../SearchBar/SearchBar";
import { LegisLoopLogo } from "../Icons/Icons";

function NavBar() {
    const location = useLocation();
    const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

    const navItems = [
        { path: "/", label: "LegisLoop" },
        { path: "/legislation", label: "Legislation" },
        { path: "/findrepresentatives", label: "Find Your Representatives" },
        { path: "/connect", label: "Connect" },
    ];

    const toggleMobileMenu = () => {
        setIsMobileMenuOpen(!isMobileMenuOpen);
    };

    return (
        <nav className="bg-custom-blue text-[#fff6ec] sticky top-0 left-0 right-0 z-10 shadow-md shadow-blue-gray-900/5">
            <div className="flex items-center justify-between h-16 px-6">
                <div className="flex items-center space-x-6">
                    <Link to="/" className="flex items-center">
                        <LegisLoopLogo className="size-11 mr-4" />
                    </Link>
                    <ul className="hidden lg:flex space-x-4">
                        {navItems.slice(0, 3).map((item) => (
                            <li key={item.path}>
                                <Link
                                    to={item.path}
                                    className={`relative flex px-3 py-2 transition-all duration-300
                    before:content-[''] before:absolute before:w-full before:h-[2px]
                    before:bottom-0 before:left-0 before:bg-[#fff6ec] before:scale-x-0
                    before:transition-transform before:duration-300 before:ease-in-out
                    hover:before:scale-x-100 ${
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
                <div className="hidden lg:flex items-center space-x-6">
                    <SearchBar />
                    <ul className="flex space-x-4">
                        <li>
                            <Link
                                to="/connect"
                                className={`relative flex px-3 py-2 transition-all duration-300
                  before:content-[''] before:absolute before:w-full before:h-[2px]
                  before:bottom-0 before:left-0 before:bg-[#fff6ec] before:scale-x-0
                  before:transition-transform before:duration-300 before:ease-in-out
                  hover:before:scale-x-100 ${
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
                <div className="lg:hidden">
                    <button onClick={toggleMobileMenu} className="text-[#fff6ec] focus:outline-none">
                        <svg
                            className="w-6 h-6"
                            fill="none"
                            stroke="currentColor"
                            strokeWidth={2}
                            viewBox="0 0 24 24"
                        >
                            <path strokeLinecap="round" strokeLinejoin="round" d="M4 6h16M4 12h16M4 18h16" />
                        </svg>
                    </button>
                </div>
            </div>
            {isMobileMenuOpen && (
                <div className="lg:hidden px-6 pb-4">
                    <div className="mb-4">
                        <SearchBar />
                    </div>
                    <ul className="flex flex-col space-y-2">
                        {navItems.map((item) => (
                            <li key={item.path}>
                                <Link
                                    to={item.path}
                                    onClick={() => setIsMobileMenuOpen(false)}
                                    className={`block px-4 py-2 rounded transition-colors duration-300 ${
                                        location.pathname === item.path
                                            ? "bg-[#fff6ec] text-custom-blue font-bold"
                                            : "hover:bg-[#fff6ec] hover:text-custom-blue text-[#fff6ec]"
                                    }`}
                                >
                                    {item.label}
                                </Link>
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </nav>
    );
}

export default NavBar;
