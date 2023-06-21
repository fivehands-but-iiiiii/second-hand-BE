import { useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';

import { setStorageValue } from '@utils/sessionStorage';

const BASE_URL = import.meta.env.VITE_APP_BASE_URL;

interface UserGithubInfo {
  login: string;
  avatar_url: string;
}

const OAuthCallback = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const OAUTH_URL = `${BASE_URL}/git/login`;
  const JOIN_URL = `${BASE_URL}/join`;
  const queryCode = new URLSearchParams(location.search).get('code');

  // TODO: custom hook으로 분리하기 (useLogin, 로딩중일때 화면도 필요함!!)
  const postJoin = async (user: UserGithubInfo) => {
    try {
      const response = await fetch(JOIN_URL, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          memberId: user.login,
          profileImgUrl: user.avatar_url,
          regions: [
            // TODO: 지역설정 추가하여 POST 요청
            // {
            //   id: `${id}`,
            //   onFocus: true,
            // },
          ],
        }),
      });
      const jsonResponse = await response.json();
      const userInfo: UserGithubInfo = jsonResponse.body;
      return userInfo;
    } catch (error) {
      console.error(error);
      throw error;
    }
  };

  useEffect(() => {
    const getToken = async () => {
      try {
        const response = await fetch(`${OAUTH_URL}?code=${queryCode}`, {
          method: 'GET',
        });
        if (response.status === 200) {
          // 상태코드 200 : 가입된 유저
          const jsonResponse = await response.json();
          const userInfo: UserGithubInfo = jsonResponse.data;
          setStorageValue({ key: 'userInfo', value: userInfo });
          navigate('/');
          // console.log(userInfo, '200번 코드');
        } else if (response.status === 401) {
          // TODO: 상태코드 401 : 가입되지 않은 유저 -> 회원가입 포탈 띄우기
          const jsonResponse = await response.json();
          const gitUserInfo: UserGithubInfo = jsonResponse.body;
          const userInfo: UserGithubInfo = await postJoin(gitUserInfo);
          // console.log(userInfo, '401번 코드');
        }
      } catch (error) {
        console.error(error);
      }
    };
    getToken();
  }, [location, navigate, queryCode]);
  return <></>;
};

export default OAuthCallback;
