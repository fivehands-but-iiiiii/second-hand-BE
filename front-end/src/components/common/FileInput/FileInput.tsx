import { useState, ChangeEvent, useRef, Dispatch, SetStateAction } from 'react';

import Icon from '@assets/Icon';
import Button from '@common/Button/Button';

import { styled } from 'styled-components';

interface FileInputProps {
  fileCount?: string;
  setUploadImg: Dispatch<SetStateAction<string>>;
  handleUpload?: () => void;
}

const FileInput = ({
  fileCount,
  setUploadImg,
  handleUpload,
}: FileInputProps) => {
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [selectedFile, setSelectedFile] = useState<File | undefined>(undefined);
  const [previewURL, setPreviewURL] = useState('');

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
        setUploadImg(reader.result as string);
        handleUpload && handleUpload();
      };
      reader.readAsDataURL(file);
    } else {
      setPreviewURL('');
      setUploadImg('');
    }
  };

  return (
    <MyFileInput>
      <MyFileButton icon onClick={handleClick}>
        <Icon name="camera" size="xl" />
        {fileCount && <label>{fileCount}</label>}
      </MyFileButton>
      <input
        type="file"
        ref={fileInputRef}
        // accept="image/jpg, image/png, image/jpeg"
        onChange={handleFileChange}
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

export default FileInput;
