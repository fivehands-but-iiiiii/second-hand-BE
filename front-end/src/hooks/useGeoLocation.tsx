import { useEffect, useState } from 'react';

import api from '../api';

const GOOGLE_KEY = import.meta.env.VITE_GOOGLE_KEY;

interface LocationType {
  loaded: boolean;
  coordinates?: {
    lat: number;
    lng: number;
  };
  address?: string;
  error?: {
    code: number;
    message: string;
  };
}

const useGeoLocation = () => {
  const [location, setLocation] = useState<LocationType>({
    loaded: false,
    coordinates: { lat: 0, lng: 0 },
    address: '',
  });

  const onSuccess = async (location: {
    coords: { latitude: number; longitude: number };
  }) => {
    try {
      const { data } = await api.get(
        `https://maps.googleapis.com/maps/api/geocode/json?latlng=${location.coords.latitude},${location.coords.longitude}&language=ko&key=${GOOGLE_KEY}`,
      );
      const formattedAddress = data.results[5].formatted_address;
      setLocation({
        loaded: true,
        coordinates: {
          lat: location.coords.latitude,
          lng: location.coords.longitude,
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
