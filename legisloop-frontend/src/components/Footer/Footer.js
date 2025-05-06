/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * Footer Declaration.
 ****************************************************************
 * Last Updated: February 19, 2025.
 ***************************************************************/
import { DiscordIcon, EmailIcon, GitHubIcon, LinkedInIcon, YouTubeIcon } from "../Icons/Icons";
function Footer() {
    return (
        <footer className="relative z-10 bg-[#A69B97] pb-5 pt-10 sm:pt-20 lg:pb-8 dark:bg-dark flex flex-col mt-auto bottom-0">
            <div className="container mx-auto max-w-full px-4 overflow-hidden">
                <div className="-mx-4 flex flex-wrap">
                    <div className="w-full px-4 sm:w-2/3 lg:w-3/12 text-center sm:text-left">
                        <div className="mb-10 w-full pl-2">
                            <p className="mb-7 text-base text-body-color dark:text-dark-6">
                                Have any questions about LegisLoop? Stay in the loop and connect
                                with the Developers.
                            </p>
                            <p className="flex items-center justify-center sm:justify-start text-sm font-medium text-dark dark:text-white">
                                <span className="mr-3 text-primary">
                                    <EmailIcon/>
                                </span>
                                <span>contact@legisloop.net</span>
                            </p>
                        </div>
                    </div>
                    <div className="w-full px-4 sm:w-1/2 lg:w-2/12 text-center sm:text-left">
                        <div className="mb-10 w-full">
                            <h4 className="mb-9 text-lg font-semibold text-dark dark:text-white">
                                Resources
                            </h4>
                            <ul className="space-y-3">
                                <li>
                                    <a href="/" className="inline-block text-base leading-loose text-body-color hover:text-primary dark:text-dark-6">
                                        Our Products
                                    </a>
                                </li>
                                <li>
                                    <a href="/" className="inline-block text-base leading-loose text-body-color hover:text-primary dark:text-dark-6">
                                        User Strategy
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div className="w-full px-4 sm:w-1/2 lg:w-2/12 text-center sm:text-left">
                        <div className="mb-10 w-full">
                            <h4 className="mb-9 text-lg font-semibold text-dark dark:text-white">
                                Company
                            </h4>
                            <ul className="space-y-3">
                                <li>
                                    <a href="/" className="inline-block text-base leading-loose text-body-color hover:text-primary dark:text-dark-6">
                                        About LegisLoop
                                    </a>
                                </li>
                                <li>
                                    <a href="/" className="inline-block text-base leading-loose text-body-color hover:text-primary dark:text-dark-6">
                                        Contact & Support
                                    </a>
                                </li>
                                <li>
                                    <a href="/" className="inline-block text-base leading-loose text-body-color hover:text-primary dark:text-dark-6">
                                        Setting & Privacy
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div className="w-full px-4 sm:w-1/2 lg:w-2/12 text-center sm:text-left">
                        <div className="mb-10 w-full">
                            <h4 className="mb-9 text-lg font-semibold text-dark dark:text-white">
                                Quick Links
                            </h4>
                            <ul className="space-y-3">
                                <li>
                                    <a href="/" className="inline-block text-base leading-loose text-body-color hover:text-primary dark:text-dark-6">
                                        Premium Support
                                    </a>
                                </li>
                                <li>
                                    <a href="/" className="inline-block text-base leading-loose text-body-color hover:text-primary dark:text-dark-6">
                                        Our Services
                                    </a>
                                </li>
                                <li>
                                    <a href="/" className="inline-block text-base leading-loose text-body-color hover:text-primary dark:text-dark-6">
                                        Know Our Team
                                    </a>
                                </li>
                                <li>
                                    <a href="/" className="inline-block text-base leading-loose text-body-color hover:text-primary dark:text-dark-6">
                                        Download App
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div className="w-full px-4 sm:w-1/2 lg:w-2/12 text-center sm:text-left">
                        <div className="mb-10">
                            <h4 className="mb-9 text-lg font-semibold text-dark dark:text-white">
                                Connect with Us On
                            </h4>
                            <div className="mb-6 flex justify-center sm:justify-start items-center">
                                <a href="https://github.com/LegisLoop/LegisLoop" target="_blank" rel="noopener noreferrer" className="mr-3 flex h-8 w-8 items-center justify-center rounded-full border border-stroke text-dark hover:border-primary hover:bg-primary hover:text-white sm:mr-4 dark:border-dark-3 dark:text-white dark:hover:border-primary">
                                    <GitHubIcon/>
                                </a>
                                <a href="https://discord.gg/j57CwYh4" target="_blank" rel="noopener noreferrer" className="mr-3 flex h-8 w-8 items-center justify-center rounded-full border border-stroke text-dark hover:border-primary hover:bg-primary hover:text-white sm:mr-4 dark:border-dark-3 dark:text-white dark:hover:border-primary">
                                    <DiscordIcon/>
                                </a>
                                <a href="https://www.youtube.com/watch?v=5I7bzg65cyU" target="_blank" rel="noopener noreferrer" className="mr-3 flex h-8 w-8 items-center justify-center rounded-full border border-stroke text-dark hover:border-primary hover:bg-primary hover:text-white sm:mr-4 dark:border-dark-3 dark:text-white dark:hover:border-primary">
                                    <YouTubeIcon/>
                                </a>
                                <a href="https://www.linkedin.com/company/legisloop" target="_blank" rel="noopener noreferrer" className="mr-3 flex h-8 w-8 items-center justify-center rounded-full border border-stroke text-dark hover:border-primary hover:bg-primary hover:text-white sm:mr-4 dark:border-dark-3 dark:text-white dark:hover:border-primary">
                                    <LinkedInIcon/>
                                </a>
                            </div>
                            <p className="text-base text-body-color dark:text-dark-6">
                                &copy; 2025 LegisLoop
                            </p>
                        </div>
                    </div>
                </div>
            </div>
            <div>
                <span className="absolute bottom-0 left-0 z-[-1]">
                    <svg
                        width="217"
                        height="229"
                        viewBox="0 0 217 229"
                        fill="none"
                        xmlns="http://www.w3.org/2000/svg"
                    >
                        <path
                            d="M-64 140.5C-64 62.904 -1.096 1.90666e-05 76.5 1.22829e-05C154.096 5.49924e-06 217 62.904 217 140.5C217 218.096 154.096 281 76.5 281C-1.09598 281 -64 218.096 -64 140.5Z"
                            fill="url(#paint0_linear_1179_5)"
                        />
                        <defs>
                            <linearGradient
                                id="paint0_linear_1179_5"
                                x1="76.5"
                                y1="281"
                                x2="76.5"
                                y2="1.22829e-05"
                                gradientUnits="userSpaceOnUse"
                            >
                                <stop stopColor="#3056D3" stopOpacity="0.08"/>
                                <stop offset="1" stopColor="#C4C4C4" stopOpacity="0"/>
                            </linearGradient>
                        </defs>
                    </svg>
                </span>
            </div>
        </footer>
    );
}

export default Footer;
