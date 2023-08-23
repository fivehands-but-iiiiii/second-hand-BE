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
  const [userId, setUserId] = useState('');
  const [validationMessage, setValidationMessage] = useState('');
  const [isLogoutAlertOpen, setIsLogoutAlertOpen] = useState(false);
  const storedUserInfo = getStoredValue({ key: 'userInfo' });
  const isLogin = !!storedUserInfo;
  const OAUTH_URL = `https://github.com/login/oauth/authorize?client_id=${CLIENT_ID}&redirect_url=${REDIRECT_URL}`;
  const { request } = useAPI();

  const handleLogin = async () => {
    if (!inputRef.current) return;
    const { data } = await request({
      url: '/login',
      method: 'post',
      config: {
        data: {
          memberId: inputRef.current.value,
        },
      },
    });
    if (!data) return;
    setStorageValue({
      key: 'userInfo',
      value: {
        id: data.id,
        memberId: data.memberId,
        profileImgUrl: data.profileImgUrl,
        regions: data.regions,
      },
    });
    setStorageValue({
      key: 'token',
      value: data.token,
    });
    navigate('/');
  };

  const handleLogout = () => setIsLogoutAlertOpen(true);

  const logout = () => {
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
    const isInvalid = /[^0-9a-z]/.test(value);
    const isValidLength = /^(?:.{6,12}|)$/.test(value);

    if (isInvalid) return '영문 소문자와 숫자만 입력하세요';
    if (!isValidLength) return '6~12자 이내로 입력하세요';
    return '';
  };

  const { handleKeyDown } = useEnterKeyPress({ onEnterPress: handleLogin });

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
            <MyButtons>
              <Button active fullWidth onClick={handleLogout}>
                로그아웃
              </Button>
            </MyButtons>
            <Alert isOpen={isLogoutAlertOpen}>
              <Alert.Title>{ALERT_TITLE.LOGOUT}</Alert.Title>
              <Alert.Button>{alertButtons(ALERT_ACTIONS.LOGOUT)}</Alert.Button>
            </Alert>
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
            <MyButtons>
              <LoginButtons
                OAUTH_URL={OAUTH_URL}
                onLoginButton={handleLogin}
                onCreateAccount={handleCreateAccount}
              />
            </MyButtons>
          </>
        )}
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
