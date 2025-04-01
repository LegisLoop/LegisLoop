import { useState, useEffect } from "react";
import axios from 'axios';
import { stateMapping } from "../Constants/statemap";

function useGeoLocation() {
    const [stateId, setStateId] = useState(null);
    const [error, setError] = useState(null);

    const LIQ_BASE_URL = process.env.REACT_APP_LOCATION_IQ_BASE_URL || "";
    const API_KEY = process.env.REACT_APP_LOCATION_IQ_API_KEY || "";

    useEffect(() => {
        console.log('baseurl', LIQ_BASE_URL);
        console.log('key', API_KEY);
        // Check if geolocation is supported
        if (!navigator.geolocation) {
            console.log('Geolocation not supported');
            setError(new Error("Geolocation is not supported by your browser"));
            return;
        }

        navigator.geolocation.getCurrentPosition(
            async (position) => {
                const { latitude, longitude } = position.coords;
                try {
                    const response = await axios.get(LIQ_BASE_URL, {
                        params: {
                            key: API_KEY,
                            lat: latitude,
                            lon: longitude,
                            format: 'json',
                            normalizeaddress: 1, // makes data a bit easier to parse
                            statecode: 1 // makes the response include state abbreviation (nj, ca, etc.)
                        }
                    });
                    console.log('response', response);
                    const locationData = response.data;
                    const stateCode = locationData.address.state_code;

                    const state = stateMapping.find(item => item.state === stateCode.toUpperCase());
                    console.log('state', state);

                    if (!stateId) {
                        setStateId(state.id);
                    } else {
                        setStateId(52); // default to federal id if state can't be found properly
                    }
                } catch (err) {
                    setError(err);
                }
            },
            (err) => {
                setError(err);
            }
        );
    }, []);

    return { stateId, error };
}

export default useGeoLocation;
