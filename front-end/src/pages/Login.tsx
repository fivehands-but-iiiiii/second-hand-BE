import { useState, useEffect, useRef, ChangeEvent } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

const CLIENT_ID = import.meta.env.VITE_GITHUB_CLIENT_ID;
const REDIRECT_URL = import.meta.env.VITE_REDIRECT_URL;

import Button from '@common/Button';
import NavBar from '@common/NavBar';
import LabelInput from '@components/common/LabelInput';
import LoginButtons from '@components/login/LoginButtons';
import UserProfile from '@components/login/UserProfile';
import useAPI from '@hooks/useAPI';
import { getFormattedId } from '@utils/formatText';
import {
  setStorageValue,
  getStoredValue,
  removeStorageValue,
} from '@utils/sessionStorage';

import { styled } from 'styled-components';

const Login = () => {
  const location = useLocation();
  const joinedUserInfo = location.state;
  const navigate = useNavigate();
  const inputRef = useRef<HTMLInputElement>(null);
  const [isLogin, setIsLogin] = useState(false);
  const [userId, setUserId] = useState('');
  const [validationMessage, setValidationMessage] = useState('');
  const storedUserInfo = getStoredValue({ key: 'userInfo' });
  const OAUTH_URL = `https://github.com/login/oauth/authorize?client_id=${CLIENT_ID}&redirect_url=${REDIRECT_URL}`;
  const { response, error, request } = useAPI();

  const handleLogin = () => {
    if (inputRef.current) {
      setUserId(inputRef.current.value);
      request({
        url: '/login',
        method: 'post',
        config: {
          data: {
            memberId: userId,
          },
        },
      });
    }
  };

  const handleLogout = () => {
    setIsLogin(false);
    removeStorageValue({ key: 'userInfo' });
  };

  const handleCreateAccount = () => {
    navigate('/join');
  };

  const handleInputChange = ({ target }: ChangeEvent<HTMLInputElement>) => {
    const { value } = target;
    const regExp = /[^0-9a-z]/;
    const regRange = /^.{6,12}$/;
    if (regExp.test(value)) {
      setValidationMessage('영문 소문자와 숫자만 입력하세요');
      return;
    }
    if (value.length < 3) {
      setValidationMessage('');
    } else if (!regRange.test(value)) {
      setValidationMessage('6~12자 이내로 입력하세요');
    } else setValidationMessage('');
    const inputValue = value;
    const formattedId = getFormattedId(inputValue);
    const formattedValue = formattedId ? formattedId : inputValue;
    setUserId(formattedValue);
  };

  useEffect(() => {
    if (response) {
      setStorageValue({ key: 'userInfo', value: response.data });
      navigate('/');
    } else if (error) {
      if (userId) setValidationMessage(error.message);
    }
  }, [response, error]);

  useEffect(() => {
    setIsLogin(!!storedUserInfo);
  }, [storedUserInfo]);

  useEffect(() => {
    if (!joinedUserInfo) return;
    setUserId(joinedUserInfo?.memberId);
    setValidationMessage(joinedUserInfo?.validationMessage);
  }, []);

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
              value={userId}
              subText={validationMessage}
              ref={inputRef}
              onChange={handleInputChange}
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
  padding: 5vh 2.7vw;
  height: calc(90vh - 83px);
  min-height: 400px;
`;

const MyButtons = styled.div`
  display: flex;
  flex-direction: column;
  height: 170px;
  gap: 10px;
`;

export default Login;
