import { useCallback, useState, useEffect } from 'react';

import NavBar from '@common/NavBar/NavBar';
import PortalLayout from '@components/layout/PortalLayout';
import { RegionInfo } from '@components/login/Join';
import useAPI from '@hooks/useAPI';
import useGeoLocation, { coordsType } from '@hooks/useGeoLocation';
import { GoogleMap, useJsApiLoader, Marker } from '@react-google-maps/api';
import { getStoredValue, setStorageValue } from '@utils/sessionStorage';

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
  const userInfo = getStoredValue({ key: 'userInfo' });
  const [updatedRegions, setUpdatedRegions] = useState<RegionInfo[]>(regions);
  const [, setMap] = useState(null);
  const [center, setCenter] = useState<coordsType>({
    lat: 37.5000776,
    lng: 127.0385419,
  });
  const { getCoordinatesFromAddress } = useGeoLocation();
  const { request } = useAPI();

  const { isLoaded } = useJsApiLoader({
    id: 'google-map-script',
    googleMapsApiKey: GOOGLE_KEY,
  });

  const onLoad = useCallback((map: any) => {
    const bounds = new window.google.maps.LatLngBounds(center);
    map.fitBounds(bounds);
    setMap(map);
  }, []);

  const onUnmount = useCallback(() => {
    setMap(null);
  }, []);

  const handleSettingRegions = async () => {
    const prevRegions = regions;
    const isSame = areRegionsSame(prevRegions, updatedRegions);
    const isSwitched = haveSwitchedRegions(prevRegions, updatedRegions);
    const isChanged = haveChangedRegions(prevRegions, updatedRegions);

    if (isSame) return onPortal();
    if (!isSwitched && !isChanged) return;

    const endpoint = '/members/region';
    const method = isSwitched ? 'patch' : 'put';
    const requestBody = { id: userInfo.id, regions: updatedRegions };
    const updatedUserAccount = { ...userInfo, regions: updatedRegions };
    try {
      const { data } = await request({
        url: endpoint,
        method: method,
        config: { data: requestBody },
      });
      if (data) {
        setStorageValue({ key: 'userInfo', value: updatedUserAccount });
        onPortal();
      }
      return;
    } catch (error) {
      console.error(error);
    }
  };

  const areRegionsSame = (prev: RegionInfo[], updated: RegionInfo[]) => {
    return prev.every((prevRegion) =>
      updated.some(
        (updatedRegion) =>
          prevRegion.id === updatedRegion.id &&
          prevRegion.onFocus === updatedRegion.onFocus,
      ),
    );
  };

  const haveSwitchedRegions = (prev: RegionInfo[], updated: RegionInfo[]) => {
    return (
      prev.length === updated.length &&
      prev.every(
        (prevRegion, index) => prevRegion.onFocus !== updated[index].onFocus,
      )
    );
  };

  const haveChangedRegions = (prev: RegionInfo[], updated: RegionInfo[]) => {
    if (prev.length !== updated.length) return true;
    return (
      prev.some(
        (prevRegion) =>
          !updated.some((updated) => prevRegion.id === updated.id),
      ) ||
      !prev.every((prevRegion, index) => prevRegion.id === updated[index].id)
    );
  };

  const handleUpdateRegions = (regions: RegionInfo[]) => {
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
        left={<button onClick={handleSettingRegions}>닫기</button>}
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
          onSetRegions={handleUpdateRegions}
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
