import { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import Icon from '@assets/Icon';
import Button from '@common/Button';
import NavBar from '@common/NavBar';
import { setStorageValue } from '@utils/sessionStorage';

import { styled } from 'styled-components';

import api from '../../api';

import IdInput from './IdInput';
import { UserInfo } from './OAuthCallback';
import UserProfile from './UserProfile';

const Join = () => {
  const navigate = useNavigate();
  const OAuthInfo = useLocation();
  const [userInfo, setUserInfo] = useState<UserInfo | undefined>(
    OAuthInfo.state,
  );
  const [uploadImgPath, setUploadImgPath] = useState<FormData | undefined>(
    undefined,
  );
  const [validIdInfo, setValidIdInfo] = useState('');
  const [inputId, setInputId] = useState('');
  const [userAccount, setUserAccount] = useState({
    memberId: userInfo?.login,
    profileImgUrl: userInfo?.avatar_url || null,
    regions: [
      {
        id: 2,
        onFocus: true,
      },
    ],
  });

  const validateUserId = async (value: string) => {
    if (value.length && (value.length < 5 || value.length > 12)) {
      setValidIdInfo('5~12자 이내로 입력하세요');
    } else if (value.length >= 5 && value.length <= 12) {
      const isMemberIdAvailable = await checkUserIdAvailability();
      setValidIdInfo(
        isMemberIdAvailable
          ? '이미 사용중인 아이디예요'
          : '사용 가능한 아이디예요',
      );
    } else setValidIdInfo('');
  };

  const checkUserIdAvailability = async () => {
    try {
      const { data } = await api.get(`/join/availability?memberId=${inputId}`);
      return data.data;
    } catch (error) {
      console.error('유저 아이디 중복 체크 에러', error);
    }
  };

  // TODO: 전송조건 : 위치를 무조건 1개 이상 선택 해야함
  const handlePostUserInfo = async () => {
    try {
      const { status, data } = await api.post('/join', userAccount);
      if (status === 200) {
        setStorageValue({
          key: 'userInfo',
          value: {
            id: data.data,
            memberId: userAccount.memberId,
            profileImgUrl: userAccount.profileImgUrl,
            regin: userAccount.regions,
          },
        });
        navigate('/login');
        // TODO: home 으로 변경
      }
    } catch (error) {
      const { response } = error;
      if (response.status === 409) {
        console.log(response.data.message);
      } else if (response.status === 400) {
        console.log(response.data.message);
      } else console.log(error);
    }
  };

  const handleUploadImg = (filePath: FormData | undefined) => {
    if (!filePath) return;
    setUploadImgPath(filePath);
  };

  const handleInputChange = (value: string) => {
    validateUserId(value);
    setInputId(value);
    setUserAccount({ ...userAccount, memberId: value });
  };

  useEffect(() => {
    validateUserId(inputId);
  }, [inputId]);

  return (
    <MyBack>
      <NavBar
        left={<button onClick={() => navigate('/login')}>닫기</button>}
        center={'회원가입'}
        right={
          <button type="submit" onClick={handlePostUserInfo}>
            완료
          </button>
        }
      />
      <MyJoin>
        <MyUserInfo>
          <UserProfile
            profileImgUrl={userInfo?.avatar_url}
            memberId={userInfo?.login}
            handleUploadImg={handleUploadImg}
          />
          {!!userInfo || (
            <IdInput
              validIdInfo={validIdInfo}
              handleUserInput={handleInputChange}
            />
          )}
        </MyUserInfo>
        {/* TODO: 위치 추가 */}
        <Button fullWidth>
          <Icon name={'plus'} />
          위치추가
        </Button>
      </MyJoin>
    </MyBack>
  );
};

// TODO: Portal 생성하게 되면 빼야될 수 있음
const MyBack = styled.div`
  background-color: white;
`;

const MyUserInfo = styled.div`
  height: 200px;
`;

const MyJoin = styled.div`
  height: 100vh;
  padding: 5vh 2.7vw;
`;

export default Join;
