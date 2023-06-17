import { useState, useEffect } from 'react';

const CLIENT_ID = import.meta.env.VITE_GITHUB_CLIENT_ID;
const REDIRECT_URL = import.meta.env.VITE_REDIRECT_URL;

import Button from '@common/Button';
import NavBar from '@common/NavBar/NavBar';
import IdInput from '@components/login/IdInput';
import LoginButtons from '@components/login/LoginButtons';
import UserProfile from '@components/login/UserProfile';

import styled from 'styled-components';

const Login = () => {
  const [isLogin, setIsLogin] = useState(false);
  const [inputValue, setInputValue] = useState('');
  const [validIdInfo, setValidIdInfo] = useState('');

  const getUserInfo = sessionStorage.getItem('userInfo');
  const userInfo = getUserInfo && JSON.parse(getUserInfo);
  const LOGIN_URI = `https://github.com/login/oauth/authorize?client_id=${CLIENT_ID}&redirect_url=${REDIRECT_URL}`;

  const handleValidateId = ({
    target,
  }: React.KeyboardEvent<HTMLTextAreaElement>) => {
    const regExp = /[^0-9a-z]/g;
    const input = target as HTMLTextAreaElement;
    const { value } = input;

    if (regExp.test(value)) {
      setInputValue(value.replace(regExp, ''));
    }

    if (value.length < 6 || value.length > 12) {
      setValidIdInfo('6~12자 이내로 입력하세요');
    } else {
      setValidIdInfo('');
    }
  };

  const handleChangeInputValue = ({
    target,
  }: React.ChangeEvent<HTMLTextAreaElement>) => {
    setInputValue(target.value);
  };

  const handleGitHubLogin = () => {
    window.location.href = LOGIN_URI;
  };

  // TODO: DB에 아이디가 있는지 확인
  const handleIdLogin = () => {
    console.log('DB에 아이디가 있는지 확인');
  };

  const handleLogout = () => {
    setIsLogin(false);
    sessionStorage.removeItem('userInfo');
  };

  useEffect(() => {
    setIsLogin(!!userInfo);
  }, [userInfo]);

  return (
    <>
      <NavBar center={'내 계정'} />
      <MyLogin>
        {isLogin ? (
          <>
            <UserProfile {...userInfo} />
          </>
        ) : (
          <>
            <IdInput
              {...{
                inputValue,
                handleChangeInputValue,
                handleValidateId,
                validIdInfo,
              }}
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
              <LoginButtons {...{ handleIdLogin, handleGitHubLogin }} />
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
  padding: 10vh 2.7vw 2vh;
  min-height: 500px;
`;

const MyButtons = styled.div`
  display: flex;
  flex-direction: column;
  height: 170px;
  gap: 7px;
`;

export default Login;
