import { useState, useEffect } from 'react';

import FileInput from '@common/FileInput';

import { styled } from 'styled-components';
interface UserProfileProps {
  profileImgUrl?: string;
  memberId?: string;
  handleUploadImg: (fileURL: string) => void;
}

const UserProfile = ({
  profileImgUrl,
  memberId,
  handleUploadImg,
}: UserProfileProps) => {
  const [uploadImg, setUploadImg] = useState('');

  const handleUpload = (fileURL: string) => {
    setUploadImg(fileURL);
  };

  useEffect(() => {
    handleUploadImg(uploadImg);
  }, [uploadImg]);

  return (
    <MyUserProfile>
      {profileImgUrl ? (
        <MyUserImg src={profileImgUrl} alt={memberId} />
      ) : (
        <>
          <MyDefaultImgBox>
            {uploadImg && <MyPreviewFile src={uploadImg} alt="미리 보기" />}
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
