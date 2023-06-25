import { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

import { setStorageValue } from '@utils/sessionStorage';
import { AxiosError } from 'axios';

import api from '../../api';

export interface gitHubUserInfoProps {
  id: number;
  login: string;
  avatar_url: string;
}

const OAuthCallback = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const queryCode = new URLSearchParams(location.search).get('code');

  useEffect(() => {
    const authenticateWithSessionId = async () => {
      try {
        await api.get(`/git/login?code=${queryCode}`);
      } catch (error) {
        if (error instanceof AxiosError) {
          if (error.response?.status === 401) {
            const gitHubUserInfo: gitHubUserInfoProps =
              error.response.data.body;
            try {
              const { data } = await api.post('/login', {
                memberId: gitHubUserInfo.login,
              });
              setStorageValue({ key: 'userInfo', value: data.data });
              navigate('/');
            } catch {
              const { response } = error;
              if (response.status === 401) {
                navigate('/join', {
                  state: { ...gitHubUserInfo },
                  replace: true,
                });
              } else console.log(response.data.message);
            }
          } else console.log(error.response?.data.message);
        }
      }
    };
    authenticateWithSessionId();
  }, [location, navigate, queryCode]);

  return <></>;
};

export default OAuthCallback;
