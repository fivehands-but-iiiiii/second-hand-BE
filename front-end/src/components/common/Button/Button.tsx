import { HTMLAttributes, ReactNode } from 'react';
import { styled } from 'styled-components';

interface ButtonProps extends HTMLAttributes<HTMLButtonElement> {
  children: ReactNode;
  type?: 'FA' | 'rectangle';
  active?: boolean;
}

const Button = ({
  children,
  type = 'rectangle',
  active = false,
  ...rest
}: ButtonProps) => {
  const ButtonTypes = {
    FA: MyFAB,
    rectangle: MyRectangleButton,
  };
  const MyButton = ButtonTypes[type];

  return (
    <MyButton active={active} {...rest}>
      {children}
    </MyButton>
  );
};

const MyButton = styled.button<ButtonProps>`
  ${({ theme }) => theme.fonts.subhead}
  background: ${({ theme, active }) =>
    active
      ? theme.colors.accent.backgroundPrimary
      : theme.colors.system.background};
  color: ${({ theme, active }) =>
    active ? theme.colors.accent.text : theme.colors.accent.textWeak};
`;

const MyRectangleButton = styled(MyButton)`
  display: flex;
  width: 100%;
  padding: 16px 20px;
  justify-content: space-between;
  ${({ theme }) => theme.fonts.subhead}
  border-radius: 8px;
  border: ${({ theme, active }) =>
    active ? 'none' : `1px solid ${theme.colors.neutral.border}`};
`;

const MyFAB = styled(MyButton)`
  position: fixed;
  width: 56px;
  height: 56px;
  padding: 0;
  gap: 0 4px;
  background: ${({ theme }) => theme.colors.accent.backgroundPrimary};
  border-radius: 50%;
  ${({ theme }) => theme.fonts.subhead}
`;

export default Button;
