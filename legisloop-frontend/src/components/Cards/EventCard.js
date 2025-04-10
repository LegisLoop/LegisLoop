/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * Event Card Declaration.
 ****************************************************************
 * Last Updated: February 19, 2025.
 ***************************************************************/
import {EventBannerIcon} from "../Icons/Icons";
import { Link } from "react-router-dom";

function EventCard ({ title, description, signUpLink }){
    return (
        <div className="flex items-center gap-4 p-4 bg-white shadow-md border border-gray-200 max-w-md">
            <div className="flex items-center justify-center w-10 rounded-full bg-red-100 text-custom-red">
            <EventBannerIcon/>
            </div>
            <div className="flex-1">
                <h3 className="text-lg font-bold text-gray-900">{title}</h3>
                <p className="text-sm text-gray-500">{description}</p>
                <Link
                    to={signUpLink}
                    className="text-sm text-gray-600 underline hover:text-gray-900 transition"
                >
                    Link to sign up for event
                </Link>
            </div>
        </div>
    );
}
export default EventCard;
