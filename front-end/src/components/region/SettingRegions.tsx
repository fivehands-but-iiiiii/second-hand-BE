import { useEffect, useState } from 'react';
import { createPortal } from 'react-dom';

import { RegionInfo, UserRegion } from '@components/login/Join';

import RegionButtons from './RegionButtons';
import SearchRegions from './SearchRegions';

interface SettingRegionsProps {
  handleUserRegions: (regions: UserRegion[]) => void;
}

const SettingRegions = ({ handleUserRegions }: SettingRegionsProps) => {
  const [selectedRegions, setSelectedRegions] = useState<RegionInfo[]>([]);
  const [isSettingRegionsModalOpen, setIsSettingRegionsModalOpen] =
    useState(false);

  const settingUserRegions = (regions: RegionInfo[]) => {
    handleUserRegions(
      regions.map((region) => ({ id: region.id, onFocus: region.onFocus })),
    );
  };

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

  const handleSwitchRegion = (id: number) => {
    setSelectedRegions((prev) =>
      prev.map((region) =>
        region.id === +id
          ? { ...region, onFocus: true }
          : { ...region, onFocus: false },
      ),
    );
  };

  const handleRegionModal = () => {
    setIsSettingRegionsModalOpen((prev) => !prev);
  };

  useEffect(() => {
    settingUserRegions(selectedRegions);
  }, [selectedRegions]);

  return (
    <>
      <RegionButtons
        selectedRegions={selectedRegions}
        handleSwitchRegion={handleSwitchRegion}
        handleRemoveRegion={handleRemoveRegion}
        handleRegionModal={handleRegionModal}
      />
      {isSettingRegionsModalOpen &&
        createPortal(
          <SearchRegions
            onPortal={handleRegionModal}
            handleSelectRegion={handleSelectRegion}
          />,
          document.body,
        )}
    </>
  );
};

export default SettingRegions;
