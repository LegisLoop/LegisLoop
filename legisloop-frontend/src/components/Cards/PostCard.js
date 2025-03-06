import { useState } from "react";
import { ThumbsUp, ThumbsDown, Bookmark, Share2, User } from "lucide-react";

function PostCard(props) {
    const { username, userIcon, date, title, body, upvotes } = props;
    const [votes, setVotes] = useState(upvotes);
    const [saved, setSaved] = useState(false);
    const [voted, setVoted] = useState(null);

    const handleVote = (type) => {
        if (voted === type) {
            setVotes((prev) => (type === "up" ? prev - 1 : prev + 1));
            setVoted(null);
        } else {
            setVotes((prev) => (type === "up" ? prev + 1 : prev - 1));
            setVoted(type);
        }
    };

    return (
        <div className="bg-white shadow-md rounded-lg p-4 border border-gray-200 w-full max-w-2xl mx-auto">
            <div className="flex items-center space-x-3 mb-3">
                {userIcon ? (
                    <img
                        src={userIcon}
                        alt="User"
                        className="w-8 h-8 rounded-full object-cover"
                    />
                ) : (
                    <User className="w-8 h-8 text-gray-400" />
                )}
                <div>
                    <p className="font-semibold text-gray-700">{username}</p>
                    <p className="text-xs text-gray-500">{date}</p>
                </div>
            </div>
            <h2 className="text-lg font-bold text-gray-800 mb-2">{title}</h2>
            <p className="text-gray-700 mb-4">{body}</p>
            <div className="flex items-center justify-between text-gray-600">
                <div className="flex items-center space-x-2">
                    <button
                        onClick={() => handleVote("up")}
                        className={`p-1 rounded-md ${
                            voted === "up" ? "text-blue-500" : "hover:text-gray-900"
                        }`}
                    >
                        <ThumbsUp className="w-5 h-5" />
                    </button>
                    <span className="font-medium">{votes}</span>
                    <button
                        onClick={() => handleVote("down")}
                        className={`p-1 rounded-md ${
                            voted === "down" ? "text-red-500" : "hover:text-gray-900"
                        }`}
                    >
                        <ThumbsDown className="w-5 h-5" />
                    </button>
                </div>
                <div className="flex items-center space-x-3">
                    <button className="flex items-center space-x-1 text-gray-500 hover:text-gray-900">
                        <Share2 className="w-5 h-5" />
                        <span>Share</span>
                    </button>
                    <button
                        onClick={() => setSaved(!saved)}
                        className={`flex items-center space-x-1 ${
                            saved ? "text-blue-500" : "text-gray-500 hover:text-gray-900"
                        }`}
                    >
                        <Bookmark className="w-5 h-5" />
                        <span>{saved ? "Saved" : "Save"}</span>
                    </button>
                </div>
            </div>
        </div>
    );
}

export default PostCard;