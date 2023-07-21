import { useCallback, useState, useEffect } from 'react';

import NavBar from '@common/NavBar/NavBar';
import PortalLayout from '@components/layout/PortalLayout';
import { RegionInfo } from '@components/login/Join';
import useGeoLocation, { coordsType } from '@hooks/useGeoLocation';
import { GoogleMap, useJsApiLoader, Marker } from '@react-google-maps/api';

import { styled } from 'styled-components';

import SettingRegionSelector from './SettingRegionSelector';

const GOOGLE_KEY = import.meta.env.VITE_GOOGLE_KEY;

const OPTIONS = {
  minZoom: 4,
  maxZoom: 18,
};

const MAP_STYLE = {
  width: '100%',
  height: '100%',
};

interface SettingRegionMapProps {
  regions: RegionInfo[];
  onPortal: () => void;
}

const SettingRegionMap = ({ regions, onPortal }: SettingRegionMapProps) => {
  const [updatedRegions, setUpdatedRegions] = useState<RegionInfo[]>(regions);
  const [, setMap] = useState(null);
  const [center, setCenter] = useState<coordsType>({
    lat: 37.5000776,
    lng: 127.0385419,
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

  const onUnmount = useCallback(function callback() {
    setMap(null);
  }, []);

  const handleRegions = (regions: RegionInfo[]) => {
    setUpdatedRegions(regions);
  };

  useEffect(() => {
    const getCenter = async () => {
      const coords = await getCoordinatesFromAddress(
        updatedRegions.find(({ onFocus }) => onFocus)?.district || '역삼1동',
      );
      setCenter(coords);
    };
    getCenter();
  }, [updatedRegions]);

  return (
    <PortalLayout>
      <NavBar
        left={<button onClick={onPortal}>닫기</button>}
        center={'동네 설정'}
      />
      <MySettingRegionMap>
        {isLoaded && (
          <GoogleMap
            mapContainerStyle={MAP_STYLE}
            center={center}
            onLoad={onLoad}
            onUnmount={onUnmount}
            options={OPTIONS}
          >
            <Marker position={center}></Marker>
          </GoogleMap>
        )}
        <SettingRegionSelector
          regions={updatedRegions}
          handleRegions={handleRegions}
        />
      </MySettingRegionMap>
    </PortalLayout>
  );
};

const MySettingRegionMap = styled.div`
  height: 50vh;
  padding: 0 2.7vw;
  > div:first-child {
    margin-bottom: 10px;
  }
`;

export default SettingRegionMap;
