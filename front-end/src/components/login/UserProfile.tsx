import { useState, ChangeEvent } from 'react';

import FileInput from '@common/FileInput';
import { getPreviewURL } from '@utils/getConvertedFile';

import { styled } from 'styled-components';

import { InputFile } from './Join';

interface UserProfileProps {
  profileImgUrl?: string;
  memberId?: string;
  onChange?: (file: InputFile) => void;
}
// TODO: 등록된 이미지 없는 유저일 경우 이미지 변경 가능 버튼 생성하고, PATCH 요청
const UserProfile = ({
  profileImgUrl,
  memberId,
  onChange,
}: UserProfileProps) => {
  const [previewURL, setPreviewURL] = useState<string>('');

  const handleFileChange = async ({
    target,
  }: ChangeEvent<HTMLInputElement>) => {
    const file = target.files?.[0];
    if (!file) return;
    const newPreviewURL = await getPreviewURL(file);
    newPreviewURL && setPreviewURL(newPreviewURL);
    const newFormData: InputFile = {
      preview: newPreviewURL,
      file: file,
    };
    onChange && onChange(newFormData);
  };

  return (
    <MyUserProfile>
      {profileImgUrl ? (
        <MyUserImg src={profileImgUrl} alt={memberId} />
      ) : (
        <>
          <MyDefaultImgBox>
            {previewURL && <MyPreviewFile src={previewURL} alt="미리 보기" />}
            <FileInput onChage={handleFileChange} />
          </MyDefaultImgBox>
        </>
      )}
      {memberId && <p>{memberId}</p>}
    </MyUserProfile>
  );
};

const MyUserProfile = styled.div``;

const MyUserImg = styled.img`
  width: 80px;
  height: 80px;
  border-radius: 50%;
`;

const MyDefaultImgBox = styled.div`
  position: relative;
  width: 80px;
  height: 80px;
  margin: 0 auto;
  border-radius: 50%;
  line-height: 80px;
  border: 1px solid ${({ theme }) => theme.colors.neutral.border};
  > button {
    display: inline-block;
  }
`;

const MyPreviewFile = styled.img`
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: inherit;
`;

export default UserProfile;
