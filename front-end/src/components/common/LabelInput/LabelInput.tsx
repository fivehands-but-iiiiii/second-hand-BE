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
      <MyLabelInputBox>
        <MyLabelInput>
          <label htmlFor="labelInput">{label}</label>
          <input
            name="labelInput"
            placeholder={placeholder || `${label}를 입력하세요`}
            ref={ref}
            {...rest}
          />
        </MyLabelInput>
        {subText && <MySubText>{subText}</MySubText>}
      </MyLabelInputBox>
    );
  },
);

const MyLabelInputBox = styled.div`
  width: 100%;
  height: 120px;
`;

const MyLabelInput = styled.div`
  display: flex;
  align-items: flex-end;
  height: 100px;
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
  margin: 3px 0;
  color: ${({ theme }) => theme.colors.accent.backgroundPrimary};
  ${({ theme }) => theme.fonts.caption2};
  text-align: end;
`;

export default LabelInput;
