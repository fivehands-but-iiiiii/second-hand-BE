import { useState, useEffect, useRef, useId } from 'react';
import { useNavigate } from 'react-router-dom';

const CLIENT_ID = import.meta.env.VITE_GITHUB_CLIENT_ID;
const REDIRECT_URL = import.meta.env.VITE_REDIRECT_URL;
import Button from '@common/Button';
import NavBar from '@common/NavBar';
import LabelInput from '@components/common/LabelInput';
import LoginButtons from '@components/login/LoginButtons';
import UserProfile from '@components/login/UserProfile';
import useAPI from '@hooks/useAPI';
import {
  setStorageValue,
  getStoredValue,
  removeStorageValue,
} from '@utils/sessionStorage';

import { styled } from 'styled-components';

const Login = () => {
  const navigate = useNavigate();
  const inputRef = useRef<HTMLInputElement>(null);
  const [isLogin, setIsLogin] = useState(false);
  const [userId, setUserId] = useState('');
  const [validIdInfo, setValidIdInfo] = useState('');
  const storedUserInfo = getStoredValue({ key: 'userInfo' });
  const OAUTH_URL = `https://github.com/login/oauth/authorize?client_id=${CLIENT_ID}&redirect_url=${REDIRECT_URL}`;

  const { response, error, request } = useAPI({
    url: '/login',
    method: 'post',
    config: {
      data: {
        memberId: userId,
      },
    },
  });

  const handleLogin = () => {
    inputRef.current && setUserId(inputRef.current.value);
  };

  const handleLogout = () => {
    setIsLogin(false);
    removeStorageValue({ key: 'userInfo' });
  };

  const handleCreateAccount = () => {
    navigate('/join');
  };

  const handleInputChage = ({ target }: { target: HTMLInputElement }) => {
    !target.value && setValidIdInfo('');
  };

  useEffect(() => {
    if (userId) {
      if (userId.length < 6 || userId.length > 12) {
        setValidIdInfo('6~12자 이내로 입력하세요');
      } else request();
    } else setValidIdInfo('');
  }, [userId]);

  useEffect(() => {
    if (response) {
      setStorageValue({ key: 'userInfo', value: response.data });
      navigate('/');
    } else if (error) {
      if (userId) setValidIdInfo(error.message);
    }
  }, [response, error]);

  useEffect(() => {
    setIsLogin(!!storedUserInfo);
  }, [storedUserInfo]);

  return (
    <>
      <NavBar center={'내 계정'} />
      <MyLogin>
        {isLogin ? (
          <>
            <UserProfile {...storedUserInfo} />
          </>
        ) : (
          <>
            <LabelInput
              label={'아이디'}
              type={'text'}
              maxLength={12}
              subText={validIdInfo}
              ref={inputRef}
              onChange={handleInputChage}
            />
          </>
        )}
        <MyButtons>
          {isLogin ? (
            <Button active fullWidth onClick={handleLogout}>
              로그아웃
            </Button>
          ) : (
            <>
              <LoginButtons
                {...{ OAUTH_URL, handleLogin, handleCreateAccount }}
              />
            </>
          )}
        </MyButtons>
      </MyLogin>
    </>
  );
};

const MyLogin = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 1vh 2.7vw;
  height: calc(90vh - 83px);
`;

const MyButtons = styled.div`
  display: flex;
  flex-direction: column;
  height: 170px;
  gap: 10px;
`;

export default Login;
