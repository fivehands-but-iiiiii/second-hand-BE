import { ChangeEvent, HTMLAttributes, ReactNode } from 'react';

import Icon from '@assets/Icon';
import * as iconTypes from '@assets/svgs/index';
import palette from '@styles/colors';

import { styled } from 'styled-components';
interface InputStyleProps {
  singleLine?: boolean;
  adorned?: boolean;
}

interface InputProps extends HTMLAttributes<HTMLTextAreaElement> {
  value: string;
  type?: 'default' | 'search' | 'chat';
  singleLine?: boolean;
  adorned?: keyof typeof iconTypes;
  children?: ReactNode;
  onInputChange?: ({ target }: ChangeEvent<HTMLTextAreaElement>) => void;
}

const Input = ({
  value,
  type = 'default',
  singleLine = false,
  adorned = 'search',
  children,
  onInputChange,
  ...rest
}: InputProps) => {
  const inputTypes = {
    default: MyDefaultInput,
    search: MySearchInput,
    chat: MyChatInput,
  };
  const MyInput = inputTypes[type];

  return (
    <>
      <MyInput
        value={value}
        singleLine={singleLine}
        adorned={!!adorned}
        onChange={onInputChange}
        {...rest}
      >
        {children}
      </MyInput>
      {adorned && (
        <MyInputAdorned
          name={adorned}
          size={'md'}
          fill={palette.neutral.textWeak}
          adorned={!!adorned}
        />
      )}
    </>
  );
};

const MyInput = styled.textarea<InputStyleProps>`
  ${({ adorned }) => adorned && 'position: relative; padding-right: 30px;'}
  width: 100%;
  line-height: 1.5;
  color: ${({ theme }) => theme.colors.neutral.text};
  ${({ theme }) => theme.fonts.subhead};
  border: none;
  outline: none;
  resize: none;
  ${({ singleLine }) => singleLine && 'white-space: nowrap;'}
  ::placeholder {
    color: ${({ theme }) => theme.colors.neutral.textWeak};
  }
`;

const MyDefaultInput = styled(MyInput)`
  padding: 15px;
  border-top: 1px solid ${({ theme }) => theme.colors.neutral.border};
`;

const MySearchInput = styled(MyInput)`
  height: 36px;
  padding: 7px 30px;
  background-color: ${({ theme }) => theme.colors.system.backgroundWeak};
  border-radius: 10px;
`;

const MyInputAdorned = styled(Icon)<InputStyleProps>`
  ${({ adorned }) => adorned && 'position: absolute; top: 8px; left: 19px;'}
`;

const MyChatInput = styled(MyInput)`
  height: 36px;
  padding: 7px 13px;
  background-color: ${({ theme }) => theme.colors.neutral.background};
  border-radius: 18px;
  border: 1px solid ${({ theme }) => theme.colors.neutral.border};
`;

export default Input;
