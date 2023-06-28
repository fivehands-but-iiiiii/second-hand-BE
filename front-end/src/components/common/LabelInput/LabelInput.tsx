import { forwardRef, InputHTMLAttributes } from 'react';

import { styled } from 'styled-components';

interface LabelInputProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string;
  subText?: string;
}

interface LabelInputStyleProps {
  subText: boolean;
}

const LabelInput = forwardRef<HTMLInputElement, LabelInputProps>(
  ({ label, subText, placeholder, ...rest }, ref) => {
    return (
      <MyLabelInput subText={!!subText}>
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
  align-items: ${({ subText }) => (subText ? 'flex-end' : 'center')};
  padding: 1vh 0;
  border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
  ${({ theme }) => theme.fonts.subhead};
  > label {
    min-width: max-content;
    padding-left: ${({ subText }) => (subText ? '0' : '15px')};
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
