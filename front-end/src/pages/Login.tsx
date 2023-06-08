import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styled from 'styled-components';

export interface UserInfo {
  id: number;
  memberId: string;
  profileImgUrl: string;
  region: number[];
}

interface LoginProps {
  handleUserInfo: (userInfo: UserInfo) => void;
}

const Login = ({ handleUserInfo }: LoginProps) => {
  const [inputValue, setInputValue] = useState('');

  const fetchPost = async (inputValue: string) => {
    const navigate = useNavigate();
    const res = await fetch('/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ memberId: inputValue }),
    });
    const data = await res.json();
    if (data) {
      handleUserInfo(data);
      navigate('/');
    }
  };

  return (
    <MyLogin>
      <div>내 계정</div>
      <div>
        <div>아이디</div>
        <input
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          placeholder="아이디를 입력하세요"
        />
      </div>
      <div>
        <button onClick={() => fetchPost(inputValue)}>로그인</button>
        <button>회원가입</button>
      </div>
      <div>탭바</div>
    </MyLogin>
  );
};

export default Login;

const MyLogin = styled.div``;
