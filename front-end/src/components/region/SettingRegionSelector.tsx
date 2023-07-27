import { useEffect, useState } from 'react';
import { createPortal } from 'react-dom';

import { RegionInfo } from '@components/login/Join';

import RegionSelector from './RegionSelector';
import SearchRegions from './SearchRegions';

interface SettingRegionSelectorProps {
  regions?: RegionInfo[];
  onSetRegions: (regions: RegionInfo[]) => void;
}

// TODO: 지역 설정하고 닫기 누르면 요청하는 로직추가
const SettingRegionSelector = ({
  regions,
  onSetRegions,
}: SettingRegionSelectorProps) => {
  const [selectedRegions, setSelectedRegions] = useState<RegionInfo[]>(
    regions || [],
  );
  const [isSettingRegionsModalOpen, setIsSettingRegionsModalOpen] =
    useState(false);

  const handleSelectRegion = (id: number, district: string) => {
    if (selectedRegions.some((region) => region.id === id)) return;
    const newRegion = { id, district, onFocus: true };
    setSelectedRegions((prev) => [
      ...prev.map((region) =>
        region.onFocus ? { ...region, onFocus: false } : region,
      ),
      newRegion,
    ]);
    setIsSettingRegionsModalOpen(false);
  };

  const handleRemoveRegion = (id: number) => {
    if (selectedRegions.length === 1) return;
    setSelectedRegions((prev) =>
      prev
        .filter((region) => region.id !== +id)
        .map((region) => ({
          ...region,
          onFocus: true,
        })),
    );
  };

  const handleRegionButtonsClick = (id: number) => {
    const isSelectedId =
      selectedRegions.find((region) => region.onFocus)?.id === id;
    if (isSelectedId) handleRemoveRegion(id);
    else {
      setSelectedRegions((prev) =>
        prev.map((region) =>
          region.id === +id
            ? { ...region, onFocus: true }
            : { ...region, onFocus: false },
        ),
      );
    }
  };

  const handleRegionModal = () => {
    setIsSettingRegionsModalOpen((prev) => !prev);
  };

  useEffect(() => {
    onSetRegions(selectedRegions);
  }, [selectedRegions]);

  return (
    <>
      <RegionSelector
        selectedRegions={selectedRegions}
        onClickRegionButton={handleRegionButtonsClick}
        onClickAddButton={handleRegionModal}
      />
      {isSettingRegionsModalOpen &&
        createPortal(
          <SearchRegions
            onPortal={handleRegionModal}
            onSelectRegion={handleSelectRegion}
          />,
          document.body,
        )}
    </>
  );
};

export default SettingRegionSelector;
