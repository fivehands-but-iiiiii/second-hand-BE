import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const CLIENT_ID = import.meta.env.VITE_GITHUB_CLIENT_ID;
const REDIRECT_URL = import.meta.env.VITE_REDIRECT_URL;

import Button from '@common/Button';
import NavBar from '@common/NavBar';
import IdInput from '@components/login/IdInput';
import LoginButtons from '@components/login/LoginButtons';
import UserProfile from '@components/login/UserProfile';
import {
  setStorageValue,
  getStoredValue,
  removeStorageValue,
} from '@utils/sessionStorage';

import { styled } from 'styled-components';

import api from '../api';

const Login = () => {
  const navigate = useNavigate();
  const [isLogin, setIsLogin] = useState(false);
  const [userId, setUserId] = useState('');

  const storedUserInfo = getStoredValue({ key: 'userInfo' });
  const OAUTH_URL = `https://github.com/login/oauth/authorize?client_id=${CLIENT_ID}&redirect_url=${REDIRECT_URL}`;

  const handleUserInput = (userId: string) => {
    setUserId(userId);
  };

  const handleLogout = () => {
    setIsLogin(false);
    removeStorageValue({ key: 'userInfo' });
    location.reload();
  };

  const handleUserIdLogin = async () => {
    // const response = await fetch(`${BASE_URL}/login`, {
    //   method: 'POST',
    //   body: JSON.stringify({
    //     memberId: userId,
    //   }),
    // });
    // const userInfo = await response.json();
    // console.log(userId, '로그인완료한', userInfo);
    // setStorageValue({ key: 'userInfo', value: userInfo });

    const { data } = await api.post('/login', {
      memberId: userId,
    });
    setStorageValue({ key: 'userInfo', value: data.data });
    navigate('/');
  };

  // TODO: 페이지 이동 아닌 Portal 띄우기로 변경
  const handleCreateAccount = () => {
    navigate('/Join');
  };

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
            <IdInput handleUserInput={handleUserInput} />
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
                {...{ OAUTH_URL, handleUserIdLogin, handleCreateAccount }}
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
  padding: 5vh 2.7vw;
  height: 80vh;
`;

const MyButtons = styled.div`
  display: flex;
  flex-direction: column;
  height: 170px;
  gap: 7px;
`;

export default Login;
