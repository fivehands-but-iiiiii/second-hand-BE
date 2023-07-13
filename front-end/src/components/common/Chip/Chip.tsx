import { ItemStatus } from '@components/ItemStatus';

import { styled } from 'styled-components';

interface ChipProps {
  status: Exclude<ItemStatus, ItemStatus.ON_SALE>;
}

const Chip = ({ status }: ChipProps) => {
  const chipTypes = {
    [ItemStatus.RESERVATION]: {
      text: '예약중',
      style: MyReservedChip,
    },
    [ItemStatus.SOLD_OUT]: {
      text: '판매완료',
      style: MySoldOutChip,
    },
  };
  const MyChip = chipTypes[status].style;

  return <MyChip>{chipTypes[status].text}</MyChip>;
};

const MyChip = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: max-content;
  height: 22px;
  padding: 0 8px;
  line-height: 22px;
  border-radius: 8px;
  ${({ theme }) => theme.fonts.caption1};
`;

const MySoldOutChip = styled(MyChip)`
  border: 1px solid ${({ theme }) => theme.colors.neutral.backgroundBold};
  background: ${({ theme }) => theme.colors.neutral.border};
  color: ${({ theme }) => theme.colors.neutral.textWeak};
`;

const MyReservedChip = styled(MyChip)`
  background: ${({ theme }) => theme.colors.accent.backgroundSecondary};
  color: ${({ theme }) => theme.colors.accent.text};
`;

export default Chip;
