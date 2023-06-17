import { styled } from 'styled-components';

interface UserProfileProps {
  profileImgUrl: string;
  memberId: string;
}

const UserProfile = ({ profileImgUrl, memberId }: UserProfileProps) => {
  return (
    <MyUserProfile>
      <MyUserImg src={profileImgUrl} alt={memberId} />
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

export default UserProfile;
