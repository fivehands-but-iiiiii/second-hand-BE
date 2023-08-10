import { useState, useEffect, useRef, ChangeEvent } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

const CLIENT_ID = import.meta.env.VITE_GITHUB_CLIENT_ID;
const REDIRECT_URL = import.meta.env.VITE_REDIRECT_URL;
import Alert from '@common/Alert';
import {
  ALERT_ACTIONS,
  ALERT_TITLE,
  AlertActionsProps,
} from '@common/Alert/constants';
import Button from '@common/Button';
import NavBar from '@common/NavBar';
import LabelInput from '@components/common/LabelInput';
import LoginButtons from '@components/login/LoginButtons';
import UserProfile from '@components/login/UserProfile';
import useAPI from '@hooks/useAPI';
import useEnterKeyPress from '@hooks/useEnterKeyPress';
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
  const [isLogoutAlertOpen, setIsLogoutAlertOpen] = useState(false);
  const storedUserInfo = getStoredValue({ key: 'userInfo' });
  const OAUTH_URL = `https://github.com/login/oauth/authorize?client_id=${CLIENT_ID}&redirect_url=${REDIRECT_URL}`;
  const { response, error, request } = useAPI();

  const handleLogin = () => {
    if (!inputRef.current) return;
    request({
      url: '/login',
      method: 'post',
      config: {
        data: {
          memberId: inputRef.current.value,
        },
      },
    });
  };

  const handleLogout = () => setIsLogoutAlertOpen(true);

  const logout = () => {
    setIsLogin(false);
    removeStorageValue('userInfo', 'token');
    setIsLogoutAlertOpen(false);
  };

  const handleAlert = (type: AlertActionsProps['id']) => {
    if (type !== 'cancel' && type !== 'logout') return;
    const actions = {
      logout: () => logout(),
      cancel: () => setIsLogoutAlertOpen(false),
    };
    return actions[type]();
  };

  const alertButtons = (actions: AlertActionsProps[]) =>
    actions.map(({ id, action }) => (
      <button key={id} onClick={() => handleAlert(id)}>
        {action}
      </button>
    ));

  const handleCreateAccount = () => navigate('/join');

  const handleInputChange = ({ target }: ChangeEvent<HTMLInputElement>) => {
    const { value } = target;
    const validationMessage = getValidationMessage(value);
    setValidationMessage(validationMessage);

    const formattedId = getFormattedId(value);
    const formattedValue = formattedId || value;
    setUserId(formattedValue);
  };

  const getValidationMessage = (value: string) => {
    const regExp = /[^0-9a-z]/;
    const regRange = /^.{6,12}$/;
    const isInvalid = regExp.test(value);
    const isInValidRange = regRange.test(value);
    const isValidLength =
      !value.length || (value.length >= 3 && isInValidRange);
    return isInvalid
      ? '영문 소문자와 숫자만 입력하세요'
      : isValidLength
      ? ''
      : '6~12자 이내로 입력하세요';
  };

  const { handleKeyDown } = useEnterKeyPress({ onEnterPress: handleLogin });

  useEffect(() => {
    if (response) {
      setStorageValue({
        key: 'userInfo',
        value: {
          id: response.data.id,
          memberId: response.data.memberId,
          profileImgUrl: response.data.profileImgUrl,
          regions: response.data.regions,
        },
      });
      setStorageValue({
        key: 'token',
        value: response.data.token,
      });
      navigate('/');
      return;
    }

    if (error && userId) setValidationMessage(error.message);
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
              onKeyDown={handleKeyDown}
            />
          </>
        )}
        <MyButtons>
          {isLogin && (
            <Button active fullWidth onClick={handleLogout}>
              로그아웃
            </Button>
          )}
          {!isLogin && (
            <LoginButtons
              OAUTH_URL={OAUTH_URL}
              onLoginButton={handleLogin}
              onCreateAccount={handleCreateAccount}
            />
          )}
        </MyButtons>
        <Alert isOpen={isLogoutAlertOpen}>
          <Alert.Title>{ALERT_TITLE.LOGOUT}</Alert.Title>
          <Alert.Button>{alertButtons(ALERT_ACTIONS.LOGOUT)}</Alert.Button>
        </Alert>
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
`;

const MyButtons = styled.div`
  display: flex;
  flex-direction: column;
  height: 170px;
  gap: 10px;
`;

export default Login;
