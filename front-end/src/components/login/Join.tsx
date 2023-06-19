import { useState, useEffect } from 'react';

import Icon from '@assets/Icon';
import Button from '@common/Button';
import NavBar from '@common/NavBar';

import { styled } from 'styled-components';

import IdInput from './IdInput';
import UserProfile from './UserProfile';

const BASE_URL = import.meta.env.VITE_APP_BASE_URL;

// TODO: handleUpload (마지막으로 저장된 file src 저장하는 함수, 프로필에선 1개 & 새상품등록일땐 여러개 배열)
const Join = () => {
  const [inputId, setInputId] = useState('');
  const [uploadProfileImg, setUploadProfileImg] = useState('');
  const [validIdInfo, setValidIdInfo] = useState('');

  const validateUserId = async (value: string) => {
    if (value.length && (value.length < 5 || value.length > 12)) {
      setValidIdInfo('5~12자 이내로 입력하세요');
    } else if (value.length >= 5 && value.length <= 12) {
      const isMemberIdAvailable = await checkUserIdAvailability();
      setValidIdInfo(
        isMemberIdAvailable
          ? '이미 사용중인 아이디예요'
          : '사용 가능한 아이디예요',
      );
    } else setValidIdInfo('');
  };

  const checkUserIdAvailability = async () => {
    const response = await fetch(
      `${BASE_URL}/join/availability?memberId=${inputId}`,
    );
    const isMemberIdAvailable = await response.json();
    return isMemberIdAvailable.data;
  };

  // TODO: 로그인, 회원가입 fetch 로직 분리하기
  const handlePostUserInfo = async () => {
    try {
      const response = await fetch(`${BASE_URL}/join`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          memberId: inputId,
          profileImgUrl: uploadProfileImg,
          regions: [
            {
              id: 1,
              onFocus: true,
            },
          ],
        }),
      });
      const message = await response.json();
      if (message.status === 200) {
        console.log('회원가입 완료');
      } else if (message.status === 409) {
        console.log('중복아이디');
      } else if (message.status === 400) {
        console.log('형식에 맞게 입력하세요');
      }
    } catch (error) {
      console.error(error);
    }
  };

  const handleInputChange = (value: string) => {
    setInputId(value);
    validateUserId(value);
  };

  useEffect(() => {
    validateUserId(inputId);
  }, [inputId]);

  return (
    <MyBack>
      <NavBar
        left={<button onClick={() => console.log('포탈 닫기')}>닫기</button>}
        center={'회원가입'}
        right={
          <button type="submit" onClick={handlePostUserInfo}>
            완료
          </button>
        }
      />
      <MyJoin>
        <UserProfile setValue={setUploadProfileImg} />
        <IdInput setValue={handleInputChange} validIdInfo={validIdInfo} />
        <Button fullWidth>
          <Icon name={'plus'} />
          위치추가
        </Button>
      </MyJoin>
    </MyBack>
  );
};

// TODO: Portal 생성하게 되면 빼야될 수 있음
const MyBack = styled.div`
  background-color: white;
`;
const MyJoin = styled.div`
  height: 100vh;
  padding: 5vh 2.7vw;
`;

export default Join;
