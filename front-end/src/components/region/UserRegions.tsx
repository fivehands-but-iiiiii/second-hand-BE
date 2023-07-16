import { useCallback, useState, useEffect } from 'react';

import NavBar from '@common/NavBar/NavBar';
import PortalLayout from '@components/layout/PortalLayout';
import { RegionInfo } from '@components/login/Join';
import useGeoLocation, { coordsType } from '@hooks/useGeoLocation';
import { GoogleMap, useJsApiLoader, Marker } from '@react-google-maps/api';

import { styled } from 'styled-components';

import SettingRegions from './SettingRegions';

const GOOGLE_KEY = import.meta.env.VITE_GOOGLE_KEY;

const OPTIONS = {
  minZoom: 4,
  maxZoom: 18,
};

const containerStyle = {
  width: '100%',
  height: '100%',
};

interface UserRegionsProps {
  onPortal: () => void;
}

const UserRegions = ({ onPortal }: UserRegionsProps) => {
  const [userRegions, setUserRegions] = useState<RegionInfo[]>([
    {
      id: 1168064000,
      district: '역삼1동',
      onFocus: false,
    },
  ]);
  const [map, setMap] = useState(null);
  const [center, setCenter] = useState<coordsType>({
    lat: 37.490821,
    lng: 127.033417,
  });
  const { getCoordinatesFromAddress } = useGeoLocation();

  const { isLoaded } = useJsApiLoader({
    id: 'google-map-script',
    googleMapsApiKey: GOOGLE_KEY,
  });

  const onLoad = useCallback(function callback(map: any) {
    const bounds = new window.google.maps.LatLngBounds(center);
    map.fitBounds(bounds);

    setMap(map);
  }, []);

  const onUnmount = useCallback(function callback(map: any) {
    setMap(null);
  }, []);

  const handleUserRegionMaps = (regions: RegionInfo[]) => {
    setUserRegions(regions);
  };

  useEffect(() => {
    const getCenter = async () => {
      const coords = await getCoordinatesFromAddress(
        userRegions.filter(({ onFocus }) => onFocus)[0].district,
      );
      setCenter(coords);
    };
    getCenter();
  }, [userRegions]);

  return (
    <PortalLayout>
      <NavBar left={<button onClick={onPortal}>닫기</button>} />
      <MyRegionMap className="?">
        {isLoaded && (
          <GoogleMap
            mapContainerStyle={containerStyle}
            center={center}
            onLoad={onLoad}
            onUnmount={onUnmount}
            options={OPTIONS}
          >
            <Marker position={center}></Marker>
          </GoogleMap>
        )}
        <SettingRegions
          userRegions={userRegions}
          handleUserRegionMaps={handleUserRegionMaps}
        />
      </MyRegionMap>
    </PortalLayout>
  );
};

const MyRegionMap = styled.div`
  height: 50vh;
  padding: 0 2.7vw;
  > div:first-child {
    margin-bottom: 10px;
  }
`;

export default UserRegions;
