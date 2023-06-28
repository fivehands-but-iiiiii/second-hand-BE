import { forwardRef, InputHTMLAttributes } from 'react';

import { styled, css } from 'styled-components';

interface LabelInputProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string;
  subText?: string;
}

interface LabelInputStyleProps {
  subText?: boolean;
}

const LabelInput = forwardRef<HTMLInputElement, LabelInputProps>(
  ({ label, subText, placeholder, ...rest }, ref) => {
    const isSubText = typeof subText === 'string';
    return (
      <MyLabelInput subText={isSubText}>
        <label htmlFor="labelInput">{label}</label>
        <input
          name="labelInput"
          placeholder={placeholder || `${label}를 입력하세요`}
          ref={ref}
          {...rest}
        />
        {subText && <MySubText>{subText}</MySubText>}
      </MyLabelInput>
    );
  },
);

const MyLabelInput = styled.div<LabelInputStyleProps>`
  position: relative;
  display: flex;
  border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
  ${({ subText }) =>
    subText
      ? css`
          align-items: flex-end;
          padding: 1vh 0;
        `
      : css`
          align-items: center;
          > label {
            padding: 15px;
          }
        `};
  ${({ theme }) => theme.fonts.subhead};
  > label {
    min-width: max-content;
    color: ${({ theme }) => theme.colors.neutral.text};
  }
  > input {
    flex: 1;
    margin-left: 10px;
    border: none;
    outline: none;
    color: ${({ theme }) => theme.colors.neutral.text};
    &::placeholder {
      color: ${({ theme }) => theme.colors.neutral.textWeak};
    }
  }
`;

const MySubText = styled.p`
  position: absolute;
  bottom: -35px;
  right: 0;
  color: ${({ theme }) => theme.colors.accent.backgroundPrimary};
  ${({ theme }) => theme.fonts.caption2};
  text-align: end;
`;

export default LabelInput;
