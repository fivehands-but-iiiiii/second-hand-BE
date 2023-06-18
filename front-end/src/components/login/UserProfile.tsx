import Icon from '@assets/Icon';
import Button from '@common/Button/Button';

import { styled } from 'styled-components';
interface UserProfileProps {
  profileImgUrl?: string;
  memberId?: string;
}

const UserProfile = ({ profileImgUrl, memberId }: UserProfileProps) => {
  return (
    // TODO: 이미지 수정 기능 추가
    <MyUserProfile>
      {profileImgUrl ? (
        <MyUserImg src={profileImgUrl} alt={memberId} />
      ) : (
        <MyDefaultImg>
          <Button icon>
            <Icon name={'camera'} />
          </Button>
        </MyDefaultImg>
      )}
      <p>{memberId}</p>
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

const MyDefaultImg = styled.div`
  width: 80px;
  height: 80px;
  border-radius: 50%;
  line-height: 80px;
  text-align: center;
  border: 1px solid ${({ theme }) => theme.colors.neutral.border};
  > button {
    display: inline-block;
  }
`;

export default UserProfile;
