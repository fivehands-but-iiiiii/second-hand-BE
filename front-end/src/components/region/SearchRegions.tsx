import { useCallback, useEffect, useState, ChangeEvent } from 'react';

import Button from '@common/Button/Button';
import NavBar from '@common/NavBar/NavBar';
import Textarea from '@common/Textarea/Textarea';
import PortalLayout from '@components/layout/PortalLayout';
import useAPI from '@hooks/useAPI';
import useGeoLocation, { coordsType } from '@hooks/useGeoLocation';

import { styled } from 'styled-components';
export interface Region {
  id: number;
  city?: string;
  county?: string;
  district: string;
}

interface SearchRegionsProps {
  onPortal: () => void;
  handleSelectRegion: (id: number, district: string) => void;
}

const SearchRegions = ({
  onPortal,
  handleSelectRegion,
}: SearchRegionsProps) => {
  const [searchKeyword, setSearchKeyword] = useState('');
  const [address, setAddress] = useState('역삼1동');
  const [center, setCenter] = useState<coordsType>();
  const [regionList, setRegionList] = useState<Region[]>([]);
  const { request } = useAPI();
  const { location: currentLocation } = useGeoLocation();

  const getCurrentRegionList = () => {
    getCurrentLocation();
  };

  const getCurrentLocation = useCallback(() => {
    if (!currentLocation.coords || !currentLocation.address) return;
    setCenter({
      lat: currentLocation.coords.lat,
      lng: currentLocation.coords.lng,
    });
    setAddress(currentLocation.address);
  }, [currentLocation]);

  const getRegionList = async (keyword: string) => {
    const { data } = await request({
      url: `/regions?keyword=${keyword}`,
      method: 'get',
    });
    setRegionList(data);
  };

  const debounce = useCallback((callback: () => void, delay: number) => {
    let timer: ReturnType<typeof setTimeout>;
    return () => {
      clearTimeout(timer);
      timer = setTimeout(callback, delay);
    };
  }, []);

  const handleSearchChange = useCallback(
    ({ target }: ChangeEvent<HTMLTextAreaElement>) => {
      const { value } = target;
      setSearchKeyword(value);
      if (value.length < 2) return;
      debounce(() => getRegionList(value), 500)();
    },
    [debounce],
  );

  useEffect(() => {
    getRegionList(address);
  }, [address]);

  return (
    <PortalLayout>
      <NavBar left={<button onClick={onPortal}>닫기</button>}>
        <Textarea
          type={'icon'}
          icon={'search'}
          placeholder={'동명(읍, 면)으로 검색(ex. 서초동)'}
          singleLine
          rows={1}
          value={searchKeyword}
          onChange={handleSearchChange}
        />
      </NavBar>
      <MySearchRegions>
        <Button active fullWidth onClick={getCurrentRegionList}>
          현재 위치로 찾기
        </Button>
      </MySearchRegions>
      <MyRegionList>
        {regionList.length > 0 ? (
          <ul>
            {regionList.map(({ id, city, county, district }: Region) => (
              <MyRegion
                key={id}
                onClick={() => handleSelectRegion(id, district)}
              >
                {city} {county} {district}
              </MyRegion>
            ))}
          </ul>
        ) : (
          <p>
            검색 결과가 없어요. <br />
            동네 이름을 다시 확인해주세요.
          </p>
        )}
      </MyRegionList>
    </PortalLayout>
  );
};

const MySearchRegions = styled.div`
  padding: 10px 2.7vw;
`;

const MyRegionList = styled.div`
  height: calc(100vh - 194px);
  padding: 0 3vw 10px;
  text-align: center;
  color: ${({ theme }) => theme.colors.neutral.textWeak};
  ul {
    height: 100%;
    ${({ theme }) => theme.fonts.subhead}
    overflow: auto;
    > li:not(:last-child) {
      border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
    }
  }
`;

const MyRegion = styled.li`
  height: 5vh;
  min-width: 200px;
  line-height: 5vh;
  text-align: start;
  color: ${({ theme }) => theme.colors.neutral.text};
`;

export default SearchRegions;
