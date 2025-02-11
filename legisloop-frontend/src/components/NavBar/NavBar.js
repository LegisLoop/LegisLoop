/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * NavBar component declaration.
 ****************************************************************
 * Last Updated: February 10, 2025.
 ***************************************************************/
function NavBar() {
    return (
        <nav className="flex items-center justify-between bg-[#344966] text-[#fff6ec] h-16 fixed top-0 left-0 right-0 z-10">
            <div className="flex items-center">
                <ul className="flex space-x-6">
                    <li>
                        <a href="/" className="flex items-center justify-center h-full px-4 hover:scale-105 transition-all">
                            HOME
                        </a>
                    </li>
                </ul>
            </div>
            <div className="flex items-center">
                <ul className="flex space-x-6">
                    <li>
                        <a href="/" className="flex items-center justify-center h-full px-4 hover:scale-105 transition-all">
                            LEGISLATION
                        </a>
                    </li>
                    <li>
                        <a href="/" className="flex items-center justify-center h-full px-4 hover:scale-105 transition-all">
                            SEE GOVERNMENT SPENDING
                        </a>
                    </li>
                    <li>
                        <a href="/" className="flex items-center justify-center h-full px-4 hover:scale-105 transition-all">
                            FIND YOUR REPRESENTATIVES
                        </a>
                    </li>
                </ul>
            </div>
            <div className="flex items-center">
                <ul className="flex space-x-6">
                    <li>
                        <a href="/" className="flex items-center justify-center h-full px-4 hover:scale-105 transition-all">
                            CONNECT
                        </a>
                    </li>
                </ul>
            </div>
        </nav>
    );
}

export default NavBar;

