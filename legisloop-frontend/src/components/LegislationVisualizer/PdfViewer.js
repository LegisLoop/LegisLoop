import React, { useState, useRef, useEffect, useCallback } from "react";
import { Document, Page } from "react-pdf";

function PdfViewer({ file, title = "PDF Viewer" }) {
    // Hooks are declared unconditionally.
    const [numPages, setNumPages] = useState(null);
    const [jumpInput, setJumpInput] = useState("1");
    const [scale, setScale] = useState(1.0);
    const containerRef = useRef(null);
    const pageRefs = useRef([]);

    // When PDF is loaded successfully.
    const onDocumentLoadSuccess = ({ numPages }) => {
        setNumPages(numPages);
        setJumpInput("1");
        // Create an array of refs—one per page.
        pageRefs.current = Array.from({ length: numPages }, () => React.createRef());
    };

    const handleZoomIn = () => {
        setScale((prev) => prev + 0.25);
    };

    const handleZoomOut = () => {
        setScale((prev) => Math.max(prev - 0.25, 0.25));
    };

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

    // Update the jumpInput based on scroll position.
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

    return (
        <div className="w-full flex flex-col items-center">
            {/* Top Toolbar: All controls in one horizontal line */}
            <div className="w-full flex items-center justify-between mb-4 px-4">
                {/* Left: Document Title */}
                <div className="text-gray-700 font-semibold">{title}</div>
                {/* Center: Zoom and Page Controls */}
                <div className="flex items-center space-x-6">
                    <div className="flex items-center space-x-2">
                        <button
                            onClick={handleZoomOut}
                            className="px-3 py-1 bg-gray-200 rounded hover:bg-gray-300"
                        >
                            –
                        </button>
                        <span className="text-gray-700 font-semibold">
              {Math.round(scale * 100)}%
            </span>
                        <button
                            onClick={handleZoomIn}
                            className="px-3 py-1 bg-gray-200 rounded hover:bg-gray-300"
                        >
                            +
                        </button>
                    </div>
                    <div className="flex items-center space-x-2">
                        <input
                            type="number"
                            min="1"
                            max={numPages || 1}
                            value={jumpInput}
                            onChange={(e) => setJumpInput(e.target.value)}
                            onKeyDown={handleKeyDown}
                            onBlur={handleJumpToPage}
                            className="w-16 border border-gray-300 rounded px-2 py-1 text-center"
                        />
                        <span className="text-gray-700">
              / {numPages ? numPages : "-"}
            </span>
                    </div>
                </div>
                {/* Right: Download Button */}
                <a
                    href={file}
                    download
                    className="px-3 py-1 bg-gray-200 rounded hover:bg-gray-300 text-gray-700 font-semibold"
                >
                    Download
                </a>
            </div>

            {/* Scrollable PDF Container */}
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
