import { useEffect, useState } from 'react';

import axios from 'axios';

const GOOGLE_KEY = import.meta.env.VITE_GOOGLE_KEY;

export interface coordsType {
  lat: number;
  lng: number;
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
    coords: { lat: 0, lng: 0 },
    address: '',
  });

  const getAddressFromCoordinates = async (location: {
    coords: {
      latitude: number;
      longitude: number;
    };
  }) => {
    try {
      const { data } = await axios.get(
        `https://maps.googleapis.com/maps/api/geocode/json?latlng=${location.coords.latitude},${location.coords.longitude}&language=ko&key=${GOOGLE_KEY}`,
      );
      const formattedAddress = data.results[5].formatted_address;
      setLocation({
        loaded: true,
        coords: {
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

  const getCoordinatesFromAddress = async (address: string) => {
    try {
      const { data } = await axios.get(
        `https://maps.googleapis.com/maps/api/geocode/json?address=${address}&language=ko&key=${GOOGLE_KEY}`,
      );
      const coords = data.results[0].geometry.location;
      setLocation({
        loaded: true,
        coords: {
          lat: coords.lat,
          lng: coords.lng,
        },
        address: address,
      });
      return coords;
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
    navigator.geolocation.getCurrentPosition(
      getAddressFromCoordinates,
      onError,
    );
  }, []);

  return { location, getAddressFromCoordinates, getCoordinatesFromAddress };
};

export default useGeoLocation;
