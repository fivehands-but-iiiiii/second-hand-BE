import { styled } from 'styled-components';

interface SegmentedControlProps {
  options: { status: number; label: string }[];
  value: number;
  onClick: (status: number) => void;
}

interface SelectedBarProps {
  selectedIndex: number;
}

const SegmentedControl = ({
  options,
  value,
  onClick,
}: SegmentedControlProps) => {
  return (
    <MySegmentedControl>
      <MySelectedBar selectedIndex={value} />
      {options.map(({ status, label }, index) => (
        <MySegmentedButton key={status} onClick={() => onClick(index)}>
          {label}
        </MySegmentedButton>
      ))}
    </MySegmentedControl>
  );
};

const MySegmentedControl = styled.div`
  position: relative;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  width: 240px;
  height: 35px;
  padding: 2px;
  background-color: ${({ theme }) => theme.colors.neutral.backgroundBold};
  border-radius: 8px;
`;

const MySelectedBar = styled.div<SelectedBarProps>`
  position: absolute;
  width: 48%;
  height: 89%;
  left: ${({ selectedIndex }) => selectedIndex * 50 + 1}%;
  transition: left 0.3s ease-in-out;
  background-color: ${({ theme }) => theme.colors.accent.text};
  border: 0.5px solid ${({ theme }) => theme.colors.neutral.border};
  border-radius: 7px;
  box-shadow: 0px 3px 1px 0px rgba(0, 0, 0, 0.04),
    0px 3px 8px 0px rgba(0, 0, 0, 0.12);
`;

const MySegmentedButton = styled.button`
  z-index: 10;
  width: 50%;
  height: 100%;
  ${({ theme }) => theme.fonts.footnote}
`;

export default SegmentedControl;
