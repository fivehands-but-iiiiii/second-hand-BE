import { useState } from 'react';

import NavBar from '@common/NavBar/NavBar';
import SegmentedControl from '@common/SegmentedControl';
import { ItemStatus } from '@components/ItemStatus';

import BlankPage from './BlankPage';

const SALES_STATUS = [
  {
    status: ItemStatus.ON_SALE,
    label: '판매중',
  },
  {
    status: ItemStatus.SOLD_OUT,
    label: '판매완료',
  },
];

const SalesHistory = () => {
  const [selectedIndex, setSelectedIndex] = useState(ItemStatus.ON_SALE);
  const title = '판매 내역';

  const handleSelectedIndex = (index: number) => {
    setSelectedIndex(index);
  };

  return (
    <>
      <NavBar center={title}>
        <SegmentedControl
          options={SALES_STATUS}
          value={selectedIndex}
          onClick={handleSelectedIndex}
        />
      </NavBar>
      <BlankPage title={title} />
    </>
  );
};

export default SalesHistory;
