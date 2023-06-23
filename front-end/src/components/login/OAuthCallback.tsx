import { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

import { setStorageValue } from '@utils/sessionStorage';

import api from '../../api';

interface UserGithubInfo {
  id: number;
  login: string;
  avatar_url: string;
}

const OAuthCallback = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const queryCode = new URLSearchParams(location.search).get('code');

  useEffect(() => {
    const getToken = async () => {
      try {
        const { data } = await api.get(`/git/login?code=${queryCode}`);
        if (data.status === 200) {
          const userInfo: UserGithubInfo = data.data;
          setStorageValue({ key: 'userInfo', value: userInfo });
          navigate('/');
        }
      } catch (error) {
        const { response } = error;
        if (response.status === 401) {
          const gitUserInfo: UserGithubInfo = response.data.body;
          try {
            const { data } = await api.post('/login', {
              memberId: gitUserInfo.login,
            });
            setStorageValue({ key: 'userInfo', value: data.data });
            navigate('/');
          } catch {
            const { response } = error;
            if (response.status === 401) {
              navigate('/Join', { state: { ...gitUserInfo }, replace: true });
            } else console.log(response.data.message);
          }
        } else console.log(response.data.message);
      }
    };
    getToken();
  }, [location, navigate, queryCode]);

  return <></>;
};

export default OAuthCallback;
