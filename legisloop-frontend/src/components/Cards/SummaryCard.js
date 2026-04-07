
function SummaryCard({title, children}) {
    return (
        <div className="max-w-3xl mx-auto mb-4 p-4 bg-white shadow-md rounded-lg">
            {title && (
                <h2 className="text-xl font-bold text-custom-blue mb-2">
                    {title}
                </h2>
            )}
            <div className="text-sm text-gray-700 space-y-1">
                {children}
            </div>
        </div>
    );
}

export default SummaryCard;