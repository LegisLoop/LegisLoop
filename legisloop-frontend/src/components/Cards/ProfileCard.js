/***************************************************************
 * LegisLoop
 * All rights reserved (c) 2025 - GNU General Public License v3.0
 ****************************************************************
 * Representative Profile Declaration.
 ****************************************************************
 * Last Updated: February 21, 2025.
 ***************************************************************/
function ProfileCard({name, position, email, phoneNumber, serviceLength, topics}) {
    return (
        <div className="flex flex-col bg-white shadow-md border border-slate-200 rounded-lg my-6 w-96">
            <div className="p-6 text-center">
                <h4 className="mb-1 text-xl font-semibold text-slate-800">
                    {name}
                </h4>
                <p
                    className="text-sm font-semibold text-slate-500 uppercase">
                    {position}
                </p>
                <p className="text-base text-slate-600 mt-4 font-light ">
                    {email} | {phoneNumber}
                </p>
                <p className="text-base text-slate-600 mt-4 font-light ">
                    {serviceLength} + {topics}
                </p>
            </div>
        </div>
    );
}

export default ProfileCard;