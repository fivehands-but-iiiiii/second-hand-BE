import Textarea from '@common/Textarea';

import { styled } from 'styled-components';

interface IdInputProps {
  inputValue: string;
  validIdInfo: string;
  handleChangeInputValue: (
    event: React.ChangeEvent<HTMLTextAreaElement>,
  ) => void;
  handleValidateId: (event: React.KeyboardEvent<HTMLTextAreaElement>) => void;
}

const IdInput = ({
  inputValue,
  handleChangeInputValue,
  handleValidateId,
  validIdInfo,
}: IdInputProps) => {
  return (
    <div>
      <MyIdInput>
        <label htmlFor="idInput">아이디</label>
        <Textarea
          name="idInput"
          singleLine
          placeholder={'아이디를 입력하세요'}
          value={inputValue}
          maxLength={12}
          rows={1}
          onChange={handleChangeInputValue}
          onKeyUp={handleValidateId}
        />
      </MyIdInput>
      {validIdInfo && <MyLoginInfo>{validIdInfo}</MyLoginInfo>}
    </div>
  );
};

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
