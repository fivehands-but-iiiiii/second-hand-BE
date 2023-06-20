import { ChangeEvent, KeyboardEvent, useState } from 'react';

import Textarea from '@common/Textarea';

import { styled } from 'styled-components';

interface IdInputProps {
  validIdInfo?: string;
  handleUserInput: (value: string) => void;
}

const IdInput = ({ validIdInfo, handleUserInput }: IdInputProps) => {
  const [input, setInput] = useState('');

  const handleChangeInput = ({ target }: ChangeEvent<HTMLTextAreaElement>) => {
    const { value } = target;
    setInput(value);
    handleUserInput(value);
  };

  const handleFormatInput = ({
    target,
  }: KeyboardEvent<HTMLTextAreaElement>) => {
    const regExp = /[^0-9a-z]/g;
    const { value } = target as HTMLTextAreaElement;
    if (regExp.test(value)) {
      setInput(value.replace(regExp, ''));
    }
  };

  return (
    <MyInputContainer>
      <MyIdInput>
        <label htmlFor="idInput">아이디</label>
        <Textarea
          name="idInput"
          singleLine
          placeholder={'아이디를 입력하세요'}
          value={input}
          maxLength={12}
          rows={1}
          onChange={handleChangeInput}
          onKeyUp={handleFormatInput}
        />
      </MyIdInput>
      {validIdInfo && <MyLoginInfo>{validIdInfo}</MyLoginInfo>}
    </MyInputContainer>
  );
};

const MyInputContainer = styled.div`
  padding: 2vh 0;
  height: 120px;
`;

const MyIdInput = styled.div`
  display: flex;
  align-items: center;
  vertical-align: middle;
  border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
  & > label {
    min-width: 50px;
  }
`;

const MyLoginInfo = styled.p`
  margin-top: 3px;
  color: ${({ theme }) => theme.colors.accent.backgroundPrimary};
  ${({ theme }) => theme.fonts.caption2};
  text-align: end;
`;

export default IdInput;
