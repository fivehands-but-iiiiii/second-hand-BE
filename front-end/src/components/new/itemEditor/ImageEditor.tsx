import { ChangeEvent, MouseEvent } from 'react';

import Icon from '@assets/Icon';
import Button from '@common/Button';
import FileInput from '@common/FileInput';
import ImgBox from '@common/ImgBox';
import { InputFile } from '@components/login/Join';
import palette from '@styles/colors';

import { styled } from 'styled-components';

interface ImageEditorProps {
  files: InputFile[];
  onChage: (e: ChangeEvent<HTMLInputElement>) => void;
  onClick: (e: MouseEvent<HTMLButtonElement>) => void;
}

const ImageEditor = ({ files, onChage, onClick }: ImageEditorProps) => {
  return (
    <MyImagesList>
      <MyFileBox>
        <FileInput fileCount={`${files.length}/10`} onChage={onChage} />
      </MyFileBox>
      {files.map((img, index) => (
        <MyImageBox key={index}>
          <ImgBox
            key={index}
            src={img.preview}
            alt={img.file?.name || `${index} 첨부파일`}
            size="md"
          />
          {!index && <MyThumbnail>대표사진</MyThumbnail>}
          <Button value={index} icon circle={'sm'} onClick={onClick}>
            <Icon name={'x'} size={'xs'} fill={palette.neutral.background} />
          </Button>
        </MyImageBox>
      ))}
    </MyImagesList>
  );
};

const MyImagesList = styled.ul`
  display: flex;
  padding: 16px 0;
  gap: 16px;
  border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
  overflow: auto;
  -ms-overflow-style: none;
  &::-webkit-scrollbar {
    display: none;
  }
`;

const MyFileBox = styled.li`
  width: 80px;
  height: 80px;
  border-radius: 10px;
  border: 1px solid ${({ theme }) => theme.colors.neutral.border};
  object-fit: cover;
  > div {
    width: 80px;
  }
`;

const MyImageBox = styled.li`
  position: relative;
  > button {
    position: absolute;
    top: -5px;
    right: -5px;
    background-color: ${({ theme }) => theme.colors.neutral.textStrong};
    > svg {
      position: absolute;
    }
  }
`;

const MyThumbnail = styled.div`
  position: absolute;
  width: 100%;
  bottom: 0px;
  padding: 4px 8px;
  border-radius: 0 0 10px 10px;
  background-color: ${({ theme }) => theme.colors.neutral.overlay};
  color: ${({ theme }) => theme.colors.accent.text};
  ${({ theme }) => theme.fonts.caption2};
  text-align: center;
`;

export default ImageEditor;
