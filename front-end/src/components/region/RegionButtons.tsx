import { useState, useMemo, useEffect } from 'react';

import Icon from '@assets/Icon';
import Button from '@common/Button/Button';
import { RegionInfo } from '@components/login/Join';
import palette from '@styles/colors';

import styled from 'styled-components';

interface RegionButtonsProps {
  selectedRegions: RegionInfo[];
  handleSwitchRegion: (id: number) => void;
  handleRemoveRegion: (id: number) => void;
  handleRegionModal: () => void;
}

const RegionButtons = ({
  selectedRegions,
  handleSwitchRegion,
  handleRemoveRegion,
  handleRegionModal,
}: RegionButtonsProps) => {
  const [regionMessage, setRegionMessage] = useState('');
  const regionButtons = useMemo(() => {
    const selectedRegionButtons = selectedRegions.map(
      ({ id, district, onFocus }) => {
        const isActive = selectedRegions.length === 1 ? true : onFocus;
        return (
          <Button
            key={id}
            fullWidth
            active={isActive}
            onClick={() => handleSwitchRegion(id)}
          >
            {district}
            <Icon
              name={'x'}
              size={'xs'}
              fill={palette.neutral.background}
              onClick={() => handleRemoveRegion(id)}
            />
          </Button>
        );
      },
    );
    const addButton = (
      <Button fullWidth onClick={handleRegionModal}>
        <Icon name={'plus'} size={'xs'} />
        위치추가
      </Button>
    );
    return selectedRegions.length < 2
      ? [...selectedRegionButtons, addButton]
      : selectedRegionButtons;
  }, [selectedRegions]);

  useEffect(() => {
    if (selectedRegions.length < 2) {
      setRegionMessage('최소 1개 이상 최대 2개까지 선택 가능해요');
    } else setRegionMessage('');
  }, [selectedRegions.length]);

  return (
    <>
      <MyRegionButton>{regionButtons}</MyRegionButton>
      <MyRegionMessage>{regionMessage}</MyRegionMessage>
    </>
  );
};

const MyRegionButton = styled.div`
  display: flex;
  gap: 7px;
`;

const MyRegionMessage = styled.p`
  color: ${({ theme }) => theme.colors.neutral.textWeak};
  ${({ theme }) => theme.fonts.caption2};
  text-align: end;
`;

export default RegionButtons;
