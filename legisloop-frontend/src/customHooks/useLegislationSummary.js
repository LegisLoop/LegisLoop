import { useState, useEffect } from "react";
import axios from "axios";

export default function useLegislationSummary(docId, readingLevel, docContent, mimeType) {
    const [summary, setSummary] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    useEffect(() => {
        // don’t run until we have a docId and a level
        if (!docId || !readingLevel || readingLevel === "UN_EDITED") return;

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
                if (err.response?.status === 404) {
                    setSummary(null);
                    setError(new Error('No summary available for this reading level'));
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
