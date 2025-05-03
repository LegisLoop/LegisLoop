import { useState, useEffect } from "react";
import axios from "axios";

export default function useLegislationSummary(docId, readingLevel, docContent, mimeType) {
    const [summary, setSummary] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        // don’t run until we have a docId and a level
        if (!docId || !readingLevel) return;

        const source = axios.CancelToken.source();
        const fetchOrCreate = async () => {
            setLoading(true);
            setError(null);

            const baseUrl = `/api/v1/summary/readingLevel/${docId}`;
            const params = { readingLevel, mimeType };

            try {
                // 1) try GET
                const { data } = await axios.get(baseUrl, {
                    params: { readingLevel },
                    cancelToken: source.token
                });
                setSummary(data);
            } catch (err) {
                if (axios.isCancel(err)) return;
                // if it’s a 404, POST to generate a new summary
                if (err.response?.status === 404) {
                    try {
                        const { data } = await axios.post(
                            baseUrl,
                            docContent,
                            {
                                params,                            // level + mimeType
                                headers: { "Content-Type": "text/plain" },
                                cancelToken: source.token
                            }
                        );
                        setSummary(data);
                    } catch (postErr) {
                        if (!axios.isCancel(postErr)) setError(postErr);
                    }
                } else {
                    setError(err);
                }
            } finally {
                setLoading(false);
            }
        };

        fetchOrCreate();
        return () => source.cancel();
    }, [docId, readingLevel, docContent, mimeType]);

    return { summary, loading, error };
}
