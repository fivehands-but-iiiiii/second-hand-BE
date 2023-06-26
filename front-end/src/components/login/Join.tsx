import { useState, ChangeEvent, useRef, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import Icon from '@assets/Icon';
import Button from '@common/Button';
import LabelInput from '@common/LabelInput';
import NavBar from '@common/NavBar';
import useJoin from '@hooks/useJoin';
import UserInfo from '@pages/ItemDetail';
import { getFormattedId } from '@utils/formatText';

import { styled } from 'styled-components';

import api from '../../api';

import { GitHubUserInfo } from './OAuthCallback';
import UserProfile from './UserProfile';
export interface UserInfo {
  memberId: string;
  profileImgUrl?: string;
  regions: {
    id: number;
    onFocus: boolean;
  }[];
}

export interface InputFile {
  preview: string;
  file?: File;
}

const Join = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [gitHubUserInfo, setGitHubUserInfo] = useState<GitHubUserInfo>(
    location.state,
  );
  const [userInputId, setUserInputId] = useState('');
  const [validationMessage, setValidationMessage] = useState('');
  const [files, setFiles] = useState<InputFile>();
  const [regionId, setRegionId] = useState(1);
  const [userAccount, setUserAccount] = useState<UserInfo>({
    memberId: gitHubUserInfo?.login,
    profileImgUrl: gitHubUserInfo?.avatar_url,
    regions: [
      {
        id: regionId,
        onFocus: true,
      },
    ],
  });
  const [isReadyToSubmit, setIsReadyToSubmit] = useState(false);
  const [idExists, setIdExists] = useState(false);
  const timerRef = useRef<NodeJS.Timeout | undefined>(undefined);
  const { join } = useJoin();

  const handleInputChange = async ({
    target,
  }: ChangeEvent<HTMLInputElement>) => {
    const { value } = target;
    const regExp = /[^0-9a-z]/;
    if (regExp.test(value)) {
      setValidationMessage('영문 소문자와 숫자만 입력하세요');
      return;
    }
    const inputValue = value;
    const formattedId = getFormattedId(inputValue);
    const formattedValue = formattedId ? formattedId : inputValue;
    setUserInputId(formattedValue);
    if (value.length < 6) return;
    validateThrottling(value);
  };

  const validateThrottling = (value: string) => {
    clearTimeout(timerRef.current);
    const timerId = setTimeout(async () => {
      const idExists = await checkUserIdAvailability(value);
      setIdExists(idExists);
    }, 500);
    timerRef.current = timerId;
  };

  const checkUserIdAvailability = async (value: string) => {
    try {
      const { data } = await api.get(`/join/availability?memberId=${value}`);
      return data.data;
    } catch (error) {
      return false;
    }
  };

  const handleProfile = (file: InputFile) => {
    setFiles({
      preview: file.preview,
      file: file.file,
    });
  };

  const handlePostUserAccount = async () => {
    try {
      const response = await join({ files, account: userAccount });
      if (response.success) {
        navigate('/login', {
          state: {
            memberId: userInputId,
            validationMessage: '회원가입이 완료되었어요! 로그인을 진행하세요',
          },
        });
      }
    } catch (error) {
      console.error('회원가입 실패', error);
    }
  };

  useEffect(() => {
    if (userInputId.length < 3) {
      setValidationMessage('');
    } else if (userInputId.length >= 3 && userInputId.length < 6) {
      setValidationMessage('6~12자 이내로 입력하세요');
    } else if (idExists) {
      setValidationMessage('이미 사용중인 아이디예요');
      setIsReadyToSubmit(false);
    } else {
      setValidationMessage('사용 가능한 아이디예요');
      setIsReadyToSubmit(true);
      setUserAccount({ ...userAccount, memberId: userInputId });
    }
  }, [userInputId, idExists]);

  return (
    <MyBack>
      <NavBar
        left={<button onClick={() => navigate('/login')}>닫기</button>}
        center={'회원가입'}
        right={
          <button
            disabled={!isReadyToSubmit}
            type="submit"
            onClick={handlePostUserAccount}
          >
            완료
          </button>
        }
      />
      <MyJoin>
        <MyUserInfo>
          <UserProfile
            profileImgUrl={gitHubUserInfo?.avatar_url}
            memberId={gitHubUserInfo?.login}
            onChange={handleProfile}
          />
          {!!gitHubUserInfo || (
            <LabelInput
              label={'아이디'}
              subText={validationMessage}
              maxLength={12}
              value={userInputId}
              onChange={handleInputChange}
            />
          )}
        </MyUserInfo>
        <Button fullWidth>
          <Icon name={'plus'} />
          위치추가
        </Button>
      </MyJoin>
    </MyBack>
  );
};

const MyBack = styled.div`
  background-color: white;
`;

const MyUserInfo = styled.div`
  height: 200px;
  margin-bottom: 20px;
`;

const MyJoin = styled.div`
  height: 90vh;
  padding: 5vh 2.7vw;
`;

export default Join;
