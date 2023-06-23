import { useState, useEffect } from 'react';

import FileInput from '@common/FileInput/FileInput';

import { styled } from 'styled-components';

interface UserProfileProps {
  profileImgUrl?: string;
  memberId?: string;
  handleUploadImg?: (file: FormData | undefined) => void;
}

const UserProfile = ({
  profileImgUrl,
  memberId,
  handleUploadImg,
}: UserProfileProps) => {
  const [previewURL, setPreviewURL] = useState('');
  const [imgPath, setImgPath] = useState<File | undefined>(undefined);

  const handleUpload = (fileURL: string, file: File) => {
    setPreviewURL(fileURL);
    setImgPath(file);
  };
  // TODO: 사용가능한 filePath 로 POST (현재는 미리보기만 가능)
  const getFilePath = (file: File) => {
    if (!file) return;
    const formData = new FormData();
    formData.append('file', file, file.name);
    return formData;
  };

  useEffect(() => {
    handleUploadImg && handleUploadImg(imgPath && getFilePath(imgPath));
  }, [previewURL]);

  return (
    <MyUserProfile>
      {profileImgUrl ? (
        <MyUserImg src={profileImgUrl} alt={memberId} />
      ) : (
        <>
          <MyDefaultImgBox>
            {previewURL && <MyPreviewFile src={previewURL} alt="미리 보기" />}
            <FileInput handleUpload={handleUpload} />
          </MyDefaultImgBox>
        </>
      )}
      {memberId && <p>{memberId}</p>}
    </MyUserProfile>
  );
};

const MyUserProfile = styled.div`
  text-align: center;
`;

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
