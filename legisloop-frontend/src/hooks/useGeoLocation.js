import { useState, useEffect } from "react";
import axios from 'axios';
import { stateMapping } from "../Constants/statemap";

function useGeoLocation(shouldRequest) {
    const [stateId, setStateId] = useState(null);

    useEffect(() => {
        if (!shouldRequest) return;


        // Check if geolocation is supported
        if (!navigator.geolocation) {
            console.error('Geolocation not supported');
            return;
        }

        navigator.geolocation.getCurrentPosition(
            async (position) => {
                const { latitude, longitude } = position.coords;
                console.log('lat', latitude);
                console.log('long', longitude);
                try {
                    const response = await axios.get("/api/v1/location/state", {
                        params: {
                            lat: latitude,
                            lon: longitude
                        }
                    });
                    const stateCode = response.data;
                    const state = stateMapping.find(item => item.state === stateCode);

                    if (state !== undefined) {
                        setStateId(state.id);
                    } else {
                        setStateId(52); // default to federal id if state can't be found properly
                    }
                } catch (err) {
                    console.error(err)
                }
            }
        );
    }, [shouldRequest]);

    return { stateId };
}

export default useGeoLocation;
