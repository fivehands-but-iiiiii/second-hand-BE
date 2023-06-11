import { styled } from 'styled-components';

interface ChipProps {
  status: 'isReserved' | 'isSelected' | 'default';
  children: string;
}

const Chip = ({ status = 'default', children }: ChipProps) => {
  const ChipTypes = {
    isReserved: MyReservedChip,
    isSelected: MySelectedChip,
    default: MyDefaultChip,
  };
  const MyChip = ChipTypes[status];

  return <MyChip>{children}</MyChip>;
};

const MyChip = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: max-content;
`;

const MyDefaultChip = styled(MyChip)`
  height: 32px;
  padding: 0 16px;
  border: 1px solid ${({ theme }) => theme.colors.neutral.border};
  border-radius: 50px;
  ${({ theme }) => theme.fonts.caption1};
`;

const MySelectedChip = styled(MyDefaultChip)`
  border: none;
  background: ${({ theme }) => theme.colors.accent.backgroundPrimary};
`;

const MyReservedChip = styled(MyChip)`
  height: 22px;
  padding: 0 8px;
  line-height: 22px;
  color: ${({ theme }) => theme.colors.accent.text};
  border-radius: 8px;
  background: ${({ theme }) => theme.colors.accent.backgroundSecondary};
  ${({ theme }) => theme.fonts.caption1};
`;

export default Chip;
