import { useState, ChangeEvent, useRef } from 'react';

import Icon from '@assets/Icon';
import Button from '@common/Button/Button';

import { styled } from 'styled-components';

interface FileInputProps {
  fileCount?: string;
  handleUpload: (fileURL: string, file: File) => void;
}

const FileInput = ({ fileCount, handleUpload }: FileInputProps) => {
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [selectedFile, setSelectedFile] = useState<File | undefined>(undefined);
  const [previewURL, setPreviewURL] = useState('');

  // TODO: custom hook 으로 분리하면 좋겠다 !
  const handleClick = () => {
    fileInputRef.current?.click();
  };

  const handleFileChange = ({ target }: ChangeEvent<HTMLInputElement>) => {
    const file = target.files?.[0];
    setSelectedFile(file);

    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setPreviewURL(reader.result as string);
        file && handleUpload(reader.result as string, file);
      };
      reader.readAsDataURL(file);
    } else setPreviewURL('');
  };

  return (
    <MyFileInput>
      <MyFileButton icon onClick={handleClick}>
        <Icon name="camera" size="xl" />
        {fileCount && <label>{fileCount}</label>}
      </MyFileButton>
      <input type="file" ref={fileInputRef} onChange={handleFileChange} />
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

export default FileInput;
