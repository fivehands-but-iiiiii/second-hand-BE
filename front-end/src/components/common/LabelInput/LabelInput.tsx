import { forwardRef, InputHTMLAttributes } from 'react';

import { styled } from 'styled-components';

interface LabelInputProps extends InputHTMLAttributes<HTMLInputElement> {
  label: string;
  subText?: string;
  placeholder?: string;
}

const LabelInput = forwardRef<HTMLInputElement, LabelInputProps>(
  ({ label, subText, placeholder, ...rest }, ref) => {
    return (
      <MyLabelInput>
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

const MyLabelInput = styled.div`
  position: relative;
  display: flex;
  align-items: flex-end;
  padding: 1vh 0;
  border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
  ${({ theme }) => theme.fonts.subhead};
  > label {
    min-width: 50px;
  }
  > input {
    flex: 1;
    margin-left: 10px;
    border: none;
    outline: none;
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
