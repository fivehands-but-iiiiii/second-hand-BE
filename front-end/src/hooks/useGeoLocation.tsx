import { useEffect, useState } from 'react';

import axios from 'axios';

const GOOGLE_KEY = import.meta.env.VITE_GOOGLE_KEY;

export interface coordsType {
  latitude: number;
  longitude: number;
}

interface LocationType {
  loaded: boolean;
  coords?: coordsType;
  address?: string;
  error?: {
    code: number;
    message: string;
  };
}

const useGeoLocation = () => {
  const [location, setLocation] = useState<LocationType>({
    loaded: false,
    coords: { latitude: 0, longitude: 0 },
    address: '',
  });

  const onSuccess = async (location: { coords: coordsType }) => {
    try {
      const { data } = await axios.get(
        `https://maps.googleapis.com/maps/api/geocode/json?latlng=${location.coords.latitude},${location.coords.longitude}&language=ko&key=${GOOGLE_KEY}`,
      );
      const formattedAddress = data.results[5].formatted_address;
      setLocation({
        loaded: true,
        coords: {
          latitude: location.coords.latitude,
          longitude: location.coords.longitude,
        },
        address: formattedAddress,
      });
      return formattedAddress;
    } catch (error) {
      console.log(error);
    }
  };

  const onError = (error: { code: number; message: string }) => {
    setLocation({
      loaded: true,
      error,
    });
  };

  useEffect(() => {
    if (!('geolocation' in navigator)) {
      onError({
        code: 0,
        message: 'Geolocation not supported',
      });
    }
    navigator.geolocation.getCurrentPosition(onSuccess, onError);
  }, []);

  return { location, onSuccess };
};

export default useGeoLocation;
