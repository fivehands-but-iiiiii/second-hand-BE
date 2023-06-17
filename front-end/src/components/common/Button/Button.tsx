import { ComponentPropsWithRef, ReactNode, forwardRef } from 'react';

import { css, styled } from 'styled-components';

interface Circle {
  [key: string]: number;
}

interface ButtonStyleProps extends ButtonProps {
  circleSize?: number;
}

interface ButtonProps extends ComponentPropsWithRef<'button'> {
  active?: boolean;
  category?: boolean;
  icon?: boolean;
  circle?: 'sm' | 'md' | 'lg';
  fullWidth?: boolean;
  spaceBetween?: boolean;
  disabled?: boolean;
  children: ReactNode;
}

const Button = forwardRef<HTMLButtonElement, ButtonProps>(
  (
    {
      active = false,
      category = false,
      icon = false,
      circle,
      fullWidth = false,
      spaceBetween = false,
      disabled = false,
      children,
      ...rest
    }: ButtonProps,
    ref,
  ) => {
    const circleTypes = (circle: keyof Circle) => {
      const circleSize: Circle = {
        sm: 20,
        md: 28,
        lg: 56,
      };

      if (circle) {
        return circleSize[circle];
      }
    };

    return (
      <MyButton
        active={active}
        category={category}
        icon={icon}
        circleSize={circle && circleTypes(circle)}
        fullWidth={fullWidth}
        spaceBetween={spaceBetween}
        disabled={disabled}
        ref={ref}
        {...rest}
      >
        {children}
      </MyButton>
    );
  },
);

const MyButton = styled.button<ButtonStyleProps>`
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 8px 16px;
  gap: 4px;
  border-radius: 8px;
  ${({ theme }) => theme.fonts.caption1}
  ${({ active, theme }) =>
    active
      ? css`
          color: ${theme.colors.accent.text};
          background-color: ${theme.colors.accent.backgroundPrimary};
        `
      : css`
          color: ${theme.colors.accent.textWeak};
          border: 1px solid ${theme.colors.neutral.border};
        `};
  ${({ icon }) =>
    icon &&
    css`
      flex-direction: column;
      border: none;
      gap: 7px;
    `}
  ${({ circleSize, theme }) =>
    circleSize &&
    css`
      height: ${circleSize}px;
      width: ${circleSize}px;
      ${theme.fonts.caption2};
      border-radius: 50%;
      padding: 10px;
    `}
  ${({ category }) =>
    category &&
    css`
      border-radius: 50px;
    `}
  ${({ fullWidth, theme }) =>
    fullWidth &&
    css`
      width: 100%;
      padding: 16px 20px;
      ${theme.fonts.subhead};
    `}
  ${({ spaceBetween }) =>
    spaceBetween &&
    css`
      justify-content: space-between;
    `}
  ${({ disabled }) =>
    disabled &&
    css`
      pointer-events: none;
      cursor: default;
    `}
`;

export default Button;
