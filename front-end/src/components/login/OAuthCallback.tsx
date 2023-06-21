import { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

import { setStorageValue } from '@utils/sessionStorage';

import api from '../../api';
export interface UserInfo {
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
        console.log(data);
        if (data.status === 200) {
          console.log(data);
          const userInfo: UserInfo = data.data;
          setStorageValue({ key: 'userInfo', value: userInfo });
          navigate('/');
        }
        console.log(data, '200번 코드');
      } catch (error) {
        if (error.response.status === 401) {
          const gitUserInfo: UserInfo = error.response.data.body;
          navigate('/Join', { state: { ...gitUserInfo }, replace: true });
          console.log(gitUserInfo, '401번 코드 : 회원가입요망');
        } else console.log(error.response.data.message);
      }
    };
    getToken();
  }, [location, navigate, queryCode]);
  return <></>;
};

export default OAuthCallback;
