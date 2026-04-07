import React, { Suspense } from "react";
const PdfViewer = React.lazy(() => import("./PdfViewer"));

function LegislationVisualizer({ mimeType, mimeId }) {
    if (!mimeType || !mimeId) {
        return <p className="text-center text-red-500">Invalid legislation data</p>;
    }

    return (
        <div className="max-w-3xl mx-auto bg-white shadow-md rounded-lg p-4">
            {mimeType === "text/html" ? (
                <iframe
                    src={mimeId}
                    title="Legislation Viewer"
                    className="w-full h-[500px] border border-gray-300 rounded-lg"
                ></iframe>
            ) : mimeType === "application/pdf" ? (
                <div className="flex justify-center">
                    <Suspense fallback={<div className="text-blue-500">Loading PDF viewer...</div>}>
                        <PdfViewer file={mimeId} title="Document Viewer" />
                    </Suspense>
                </div>
            ) : (
                <p className="text-center text-gray-600">Unsupported file type</p>
            )}
        </div>
    );
}

export default LegislationVisualizer;
