import { useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import { setStorageValue } from '@utils/sessionStorage';
import { AxiosError } from 'axios';

import api from '../../api';
const ENV_MODE = import.meta.env.VITE_ENV_MODE;

export interface GitHubUserInfo {
  id: number;
  login: string;
  avatar_url: string;
}

const OAuthCallback = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const queryCode = new URLSearchParams(location.search).get('code');
  // const currentURL = new URL(window.location.href);
  // const queryCode = currentURL.searchParams.get('code');

  useEffect(() => {
    const authenticateWithSessionId = async () => {
      try {
        await api.get(`/git/login?code=${queryCode}&env=${ENV_MODE}`);
      } catch (error) {
        if (error instanceof AxiosError) {
          if (error.response?.status === 401) {
            const gitHubUserInfo: GitHubUserInfo = error.response.data.body;
            try {
              const { data } = await api.post('/login', {
                memberId: gitHubUserInfo.login,
              });
              setStorageValue({
                key: 'userInfo',
                value: {
                  id: data.data.id,
                  memberId: data.data.memberId,
                  profileImgUrl: data.data.profileImgUrl,
                  regions: data.data.regions,
                },
              });
              setStorageValue({
                key: 'token',
                value: data.data.token,
              });
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
  }, [location]);

  return <></>;
};

export default OAuthCallback;
