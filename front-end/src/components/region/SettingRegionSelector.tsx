import { useEffect, useState } from 'react';

import Alert from '@common/Alert';
import {
  ALERT_ACTIONS,
  ALERT_TITLE,
  AlertActionsProps,
} from '@common/Alert/constants';
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
  const [isDeleteRegionAlertOpen, setIsDeleteRegionAlertOpen] = useState(false);
  const [deleteRegionId, setDeleteRegionId] = useState(0);
  const deleteRegionDistrict =
    selectedRegions.find((region) => region.id === deleteRegionId)?.district ||
    '';

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
        region.id === id
          ? { ...region, onFocus: true }
          : { ...region, onFocus: false },
      ),
    );
  };

  const handleDeleteRegion = (id: number) => {
    if (selectedRegions.length === 1) return;
    const isSelectingRegion = selectedRegions.some(
      (region) => region.id === id && region.onFocus,
    );
    if (!isSelectingRegion) {
      handleRegionClick(id);
      return;
    }
    setIsDeleteRegionAlertOpen(true);
    setDeleteRegionId(id);
  };

  const deleteRegionAlertClose = () => {
    setIsDeleteRegionAlertOpen(false);
    setDeleteRegionId(0);
  };

  const deleteRegion = (regionId: number) => {
    setSelectedRegions((prev) =>
      prev
        .filter((region) => region.id !== regionId)
        .map((region) => ({
          ...region,
          onFocus: true,
        })),
    );
    deleteRegionAlertClose();
  };

  const handleAlert = (type: AlertActionsProps['id'], regionId: number) => {
    if (type !== 'delete' && type !== 'cancel') return;
    const actions = {
      delete: () => deleteRegion(regionId),
      cancel: () => deleteRegionAlertClose(),
    };
    return actions[type]();
  };

  const alertButtons = (actions: AlertActionsProps[], regionId: number) =>
    actions.map(({ id, action }) => (
      <button key={id} onClick={() => handleAlert(id, regionId)}>
        {action}
      </button>
    ));

  const handleRegionModal = () => setIsSettingRegionsModalOpen((prev) => !prev);

  useEffect(() => {
    onSetRegions(selectedRegions);
  }, [selectedRegions]);

  return (
    <>
      <RegionSelector
        selectedRegions={selectedRegions}
        onClickRegionButton={handleRegionClick}
        onClickDeleteButton={handleDeleteRegion}
        onClickAddButton={handleRegionModal}
      />
      {isSettingRegionsModalOpen && (
        <SearchRegions
          onPortal={handleRegionModal}
          onSelectRegion={handleSelectRegion}
        />
      )}
      <Alert isOpen={isDeleteRegionAlertOpen}>
        <Alert.Title>{ALERT_TITLE.DELETE(deleteRegionDistrict)}</Alert.Title>
        <Alert.Button>
          {alertButtons(ALERT_ACTIONS.DELETE, deleteRegionId)}
        </Alert.Button>
      </Alert>
    </>
  );
};

export default SettingRegionSelector;
