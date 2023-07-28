import { useEffect, useState } from 'react';
import { createPortal } from 'react-dom';

import { RegionInfo } from '@components/login/Join';

import RegionSelector from './RegionSelector';
import SearchRegions from './SearchRegions';

interface SettingRegionSelectorProps {
  regions?: RegionInfo[];
  onSetRegions: (regions: RegionInfo[]) => void;
}

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

  const handleRegionClick = (id: number) => {
    setSelectedRegions((prev) =>
      prev.map((region) =>
        region.id === +id
          ? { ...region, onFocus: true }
          : { ...region, onFocus: false },
      ),
    );
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
        onClickRegionButton={handleRegionClick}
        onClickRemoveButton={handleRemoveRegion}
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
