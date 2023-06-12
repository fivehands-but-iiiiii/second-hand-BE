import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { DETAIL_STATUS_MENU } from '@common/PopupSheet/sheetTypes';
import PopupSheet from '@components/common/PopupSheet/PopupSheet';

import styled from 'styled-components';

const Login = () => {
  const [inputValue, setInputValue] = useState('');
  const navigate = useNavigate();

  const fetchPost = async (inputValue: string) => {
    const res = await fetch('/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ memberId: inputValue }),
    });
    const data = await res.json();
    if (data) {
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
        <PopupSheet
          type="slideUp"
          menu={DETAIL_STATUS_MENU}
          onClose={() => console.log(1)}
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
