import {UserIcon} from "../Icons/Icons";

function VoteCard({ name, vote }) {
    return (
        <div className="flex items-center gap-4 p-4 bg-white shadow-md border border-gray-200 max-w-md">
            <div className="flex items-center justify-center w-10 rounded-full bg-red-100 text-custom-red">
                <UserIcon className={"text-custom-red"}/>
            </div>
            <div className="flex-1">
                <h3 className="text-lg font-bold text-gray-900">{name}</h3>
                <p className="text-sm text-gray-500">{vote}</p>
            </div>
        </div>
    );
}

export default VoteCard;
