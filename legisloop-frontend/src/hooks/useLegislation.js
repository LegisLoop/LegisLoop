import { useState, useEffect } from "react";
import axios from "axios";

function useLegislation(activeLevel, activeStateId, pageNumber, pageSize = 10) {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchLegislation = async () => {
            const apiId = activeLevel === "Federal" ? 52 : activeStateId;
            const url = `http://localhost:8080/api/v1/legislation/stateId/${apiId}/paginated`;

            setLoading(true);
            try {
                const response = await axios.get(url, {
                    params: {
                        page: pageNumber,
                        size: pageSize,
                    },
                });
                const newBills = response.data.content || response.data;
                setData((prevData) =>
                    pageNumber === 0 ? newBills : [...prevData, ...newBills]
                );
            } catch (err) {
                setError(err);
            } finally {
                setLoading(false);
            }
        };

        fetchLegislation();
    }, [activeLevel, activeStateId, pageNumber, pageSize]);

    return { data, loading, error };
}

export default useLegislation;
