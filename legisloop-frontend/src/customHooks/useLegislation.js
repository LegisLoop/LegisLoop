import { useState, useEffect } from "react";
import axios from "axios";

function useLegislation(activeLevel, activeStateId, pageNumber, pageSize, shouldRequest) {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchLegislation = async () => {
            console.log("activeid", activeStateId);
            const url = `/api/v1/legislation/stateId/${activeStateId}/paginated`;

            setLoading(true);
            try {
                const response = await axios.get(url, {
                    params: {
                        page: pageNumber,
                        size: pageSize,
                    },
                });
                const newBills = response.data.content || response.data;
                console.log("new bills", newBills);
                setData((prevData) =>
                    pageNumber === 0 ? newBills : [...prevData, ...newBills]
                );
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        if (shouldRequest) {
            fetchLegislation();
        }
    }, [activeLevel, activeStateId, pageNumber, pageSize, shouldRequest]);

    return { data, loading };
}

export default useLegislation;