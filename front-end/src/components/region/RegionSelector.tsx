import { useState, useMemo, useEffect } from 'react';

import Icon from '@assets/Icon';
import Button from '@common/Button/Button';
import { RegionInfo } from '@components/login/Join';
import palette from '@styles/colors';

import { styled } from 'styled-components';

interface RegionSelectorProps {
  selectedRegions: RegionInfo[];
  onClickRegionButton: (id: number) => void;
  onClickAddButton: () => void;
}

const RegionSelector = ({
  selectedRegions,
  onClickRegionButton,
  onClickAddButton,
}: RegionSelectorProps) => {
  const [regionMessage, setRegionMessage] = useState('');

  const regionButtons = useMemo(() => {
    const selectedRegionButtons = selectedRegions.map(
      ({ id, district, onFocus }) => {
        return (
          <Button
            key={id}
            fullWidth
            active={onFocus}
            onClick={() => onClickRegionButton(id)}
          >
            {district}
            <Icon name={'x'} size={'xs'} fill={palette.neutral.background} />
          </Button>
        );
      },
    );
    const addButton = (
      <Button fullWidth onClick={onClickAddButton}>
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

export default RegionSelector;
