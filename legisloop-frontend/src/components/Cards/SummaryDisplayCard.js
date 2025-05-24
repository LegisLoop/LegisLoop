import ReactMarkdown from "react-markdown";

function SummaryDisplayCard({ content }) {
    return (
        <div className="max-w-3xl mx-auto mb-6 p-4 bg-white shadow-md rounded-lg ">
            <h2 className="text-xl font-bold text-custom-blue mb-2">Bill Summary</h2>
            <div className="prose prose-sm max-w-none text-gray-800">
                {typeof content === "string" ? (
                    <ReactMarkdown>{content}</ReactMarkdown>
                ) : (
                    content
                )}
            </div>
        </div>
    );
}

export default SummaryDisplayCard;
