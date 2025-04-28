import { useState, useEffect } from "react";
import axios from "axios";

function useLegislation(
    activeLevel,
    activeStateId,
    pageNumber,
    pageSize,
    shouldRequest,
    searchTerm = "",
    activePolicy
) {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        console.log('active level', activeLevel);
        console.log('state', activeStateId);
        console.log('search term', searchTerm);
        console.log('policy', activePolicy);
        const fetchLegislation = async () => {
            setLoading(true);
            try {
                let response;
                // If a search term is provided, call the search endpoint
                if (searchTerm && searchTerm.trim() !== "") {
                    response = await axios.get("/api/v1/legislation/search", {
                        params: {
                            query: searchTerm,
                            page: pageNumber,
                            size: pageSize,
                        },
                    });
                } else if (activePolicy) {
                    console.log('Policy Search', activePolicy);
                    response = await axios.get("/api/v1/legislation/search/policy", {
                        params: {
                            policy: activePolicy,
                            page: pageNumber,
                            size: pageSize
                        }
                    });
                } else {
                    // Otherwise, call the regular paginated endpoint
                    const url = `/api/v1/legislation/stateId/${activeStateId}/paginated`;
                    response = await axios.get(url, {
                        params: {
                            page: pageNumber,
                            size: pageSize,
                        },
                    });
                }
                const newBills = response.data.content || response.data;
                setData((prevData) =>
                    pageNumber === 0 ? newBills : [...prevData, ...newBills]
                );
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        // Make the API call if either:
        // 1. A valid search term is provided, or
        // 2. Our standard condition is met (shouldRequest or default activeStateId).
        if ((searchTerm && searchTerm.trim() !== "") || shouldRequest || activeStateId === 52) {
            fetchLegislation();
        }
    }, [activeLevel, activeStateId, pageNumber, pageSize, shouldRequest, searchTerm, activePolicy]);

    return { data, loading };
}

export default useLegislation;