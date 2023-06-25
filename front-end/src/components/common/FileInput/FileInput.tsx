import { useRef, ChangeEvent } from 'react';

import Icon from '@assets/Icon';
import Button from '@common/Button/Button';

import { styled } from 'styled-components';

interface FileInputProps {
  fileCount?: string;
  onChage: (e: ChangeEvent<HTMLInputElement>) => void;
}

const FileInput2 = ({ fileCount, onChage }: FileInputProps) => {
  const fileInputRef = useRef<HTMLInputElement>(null);
  const handleInputFile = () => {
    fileInputRef.current?.click();
  };

  return (
    <MyFileInput>
      <MyFileButton icon onClick={handleInputFile}>
        <Icon name="camera" size="xl" />
        {fileCount && <label>{fileCount}</label>}
      </MyFileButton>
      <input
        type="file"
        ref={fileInputRef}
        accept="image/jpg, image/png, image/jpeg, image/gif"
        onChange={onChage}
        multiple
      />
    </MyFileInput>
  );
};

const MyFileInput = styled.div`
  width: 100%;
  height: 100%;
  border-radius: inherit;
  > input {
    display: none;
  }
`;

const MyFileButton = styled(Button)`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  position: relative;
  width: 100%;
  height: 100%;
  border-radius: inherit;
  overflow: hidden;
  cursor: pointer;
  > label {
    ${({ theme }) => theme.fonts.footnote};
    ${({ theme }) => theme.colors.neutral.textStrong};
  }
`;

export default FileInput2;
