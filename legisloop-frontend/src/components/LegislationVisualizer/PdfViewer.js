import React, { useState, useRef, useEffect, useCallback } from "react";
import { Document, Page } from "react-pdf";

function DownloadIcon({ className }) {
    return (
        <svg
            className={className}
            fill="none"
            stroke="currentColor"
            strokeWidth={2}
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
        >
            <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M4 16v2a2 2 0 002 2h12a2 2 0 002-2v-2M8 12l4 4 4-4M12 4v8"
            />
        </svg>
    );
}

function PdfViewer({ file, title = "PDF Viewer" }) {
    const [numPages, setNumPages] = useState(null);
    const [jumpInput, setJumpInput] = useState("1");
    const [scale, setScale] = useState(1.0);
    const containerRef = useRef(null);
    const pageRefs = useRef([]);

    const onDocumentLoadSuccess = ({ numPages }) => {
        setNumPages(numPages);
        setJumpInput("1");
        pageRefs.current = Array.from({ length: numPages }, () => React.createRef());
    };

    const handleZoomIn = () => setScale((prev) => prev + 0.25);
    const handleZoomOut = () => setScale((prev) => Math.max(prev - 0.25, 0.25));

    const handleJumpToPage = () => {
        const pageNum = parseInt(jumpInput, 10);
        if (
            pageNum >= 1 &&
            pageNum <= numPages &&
            pageRefs.current[pageNum - 1]?.current
        ) {
            pageRefs.current[pageNum - 1].current.scrollIntoView({
                behavior: "smooth",
                block: "start",
            });
        }
    };

    const handleKeyDown = (e) => {
        if (e.key === "Enter") {
            handleJumpToPage();
        }
    };

    const updateCurrentPage = useCallback(() => {
        if (!containerRef.current) return;
        const containerTop = containerRef.current.getBoundingClientRect().top;
        let visiblePage = 1;
        for (let i = 0; i < pageRefs.current.length; i++) {
            const pageElem = pageRefs.current[i].current;
            if (pageElem) {
                const rect = pageElem.getBoundingClientRect();
                if (rect.bottom >= containerTop + 100) {
                    visiblePage = i + 1;
                    break;
                }
            }
        }
        setJumpInput(String(visiblePage));
    }, []);

    useEffect(() => {
        const container = containerRef.current;
        if (container) {
            container.addEventListener("scroll", updateCurrentPage);
            return () => container.removeEventListener("scroll", updateCurrentPage);
        }
    }, [updateCurrentPage]);

    if (!file) {
        return <p className="text-center text-red-500">No file provided</p>;
    }

    return (
        <div className="w-full flex flex-col items-center">
            <div className="w-full flex flex-wrap items-center justify-between gap-2 mb-4 px-4 text-sm">
                <div className="text-gray-700 font-semibold">{title}</div>
                <div className="flex flex-wrap items-center justify-center gap-2">
                    <div className="flex items-center space-x-2">
                        <button
                            onClick={handleZoomOut}
                            className="px-2 py-1 bg-gray-200 rounded hover:bg-gray-300"
                        >
                            –
                        </button>
                        <span className="text-gray-700 font-semibold">
              {Math.round(scale * 100)}%
            </span>
                        <button
                            onClick={handleZoomIn}
                            className="px-2 py-1 bg-gray-200 rounded hover:bg-gray-300"
                        >
                            +
                        </button>
                    </div>

                    <div className="flex items-center space-x-1">
                        <input
                            type="number"
                            min="1"
                            max={numPages || 1}
                            value={jumpInput}
                            onChange={(e) => setJumpInput(e.target.value)}
                            onKeyDown={handleKeyDown}
                            onBlur={handleJumpToPage}
                            className="w-12 border border-gray-300 rounded px-1 py-1 text-center"
                        />
                        <span className="text-gray-700">/ {numPages || "-"}</span>
                    </div>
                </div>

                <a
                    href={file}
                    download
                    className="p-2 bg-gray-200 rounded hover:bg-gray-300 text-gray-700 font-semibold"
                    aria-label="Download PDF"
                >
                    <DownloadIcon className="w-4 h-4" />
                </a>
            </div>

            <div
                ref={containerRef}
                className="border border-gray-300 rounded-lg w-full h-[600px] overflow-auto p-4"
            >
                <Document
                    file={file}
                    onLoadSuccess={onDocumentLoadSuccess}
                    loading={<div className="text-blue-500">Loading PDF...</div>}
                    onLoadError={(err) => console.error("Error loading PDF:", err)}
                >
                    {numPages &&
                        Array.from({ length: numPages }, (_, index) => (
                            <div
                                key={`page_${index + 1}`}
                                ref={pageRefs.current[index]}
                                className="mb-8"
                            >
                                <Page
                                    pageNumber={index + 1}
                                    scale={scale}
                                    renderTextLayer={false}
                                    renderAnnotationLayer={false}
                                />
                            </div>
                        ))}
                </Document>
            </div>
        </div>
    );
}

export default PdfViewer;
