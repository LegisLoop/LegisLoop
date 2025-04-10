import { useCallback, useRef, useState, useEffect } from "react";
import * as PDFJS from "pdfjs-dist";

export default function PdfJs(props) {
    const { src, height } = props;
    const canvasRef = useRef(null);
    const [pdfDoc, setPdfDoc] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    let renderTask = null;

    const renderPage = useCallback(
        (pageNum, pdf = pdfDoc) => {
            const canvas = canvasRef.current;
            if (!canvas || !pdf) return;
            canvas.height = 0;
            canvas.width = 0;

            pdf
                .getPage(pageNum)
                .then((page) => {
                    const viewport = page.getViewport({ scale: 1.5 });
                    canvas.height = viewport.height;
                    canvas.width = viewport.width;

                    const renderContext = {
                        canvasContext: canvas.getContext("2d"),
                        viewport: viewport,
                    };

                    try {
                        if (renderTask) {
                            renderTask.cancel();
                        }
                        renderTask = page.render(renderContext);
                        return renderTask.promise;
                    } catch (error) {
                        console.error(error);
                    }
                })
                .catch((error) => console.log(error));
        },
        [pdfDoc]
    );

    useEffect(() => {
        renderPage(currentPage, pdfDoc);
    }, [pdfDoc, currentPage, renderPage]);

    useEffect(() => {
        console.log("PDF src:", src); // Check what the value is
        const loadingTask = PDFJS.getDocument({ url: src }); // wrapped in object
        loadingTask.promise.then(
            (loadedDoc) => {
                setPdfDoc(loadedDoc);
            },
            (error) => {
                console.error(error);
            }
        );
    }, [src]);

    const nextPage = () => {
        if (pdfDoc && currentPage < pdfDoc.numPages) {
            setCurrentPage(currentPage + 1);
        }
    };

    const prevPage = () => {
        if (currentPage > 1) {
            setCurrentPage(currentPage - 1);
        }
    };

    return (
        <div>
            <button onClick={prevPage} disabled={currentPage <= 1}>
                Previous
            </button>
            <button
                onClick={nextPage}
                disabled={pdfDoc ? currentPage >= pdfDoc.numPages : false}
            >
                Next
            </button>
            <canvas ref={canvasRef}></canvas>
        </div>
    );
}
