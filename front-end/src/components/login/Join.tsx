import { useState, ChangeEvent, useRef, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';

import LabelInput from '@common/LabelInput';
import NavBar from '@common/NavBar';
import SettingRegionSelector from '@components/region/SettingRegionSelector';
import useJoin from '@hooks/useJoin';
import UserInfo from '@pages/ItemDetail';
import { getFormattedId } from '@utils/formatText';

import { styled } from 'styled-components';

import api from '../../api';

import UserProfile from './UserProfile';

export interface InputFile {
  preview: string;
  file?: File;
}

export interface RegionInfo {
  id: number;
  district: string;
  onFocus: boolean;
}

export interface UserInfo {
  id?: number;
  memberId: string;
  profileImgUrl?: string;
  regions: RegionInfo[];
}

export type UserRegion = Omit<RegionInfo, 'district'>;

const Join = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const gitHubUserInfo = location.state;
  const [userInputId, setUserInputId] = useState('');
  const [validationMessage, setValidationMessage] = useState('');
  const [files, setFiles] = useState<InputFile>();
  const [userRegions, setUserRegions] = useState<RegionInfo[]>([]);
  const [userAccount, setUserAccount] = useState<UserInfo>({
    memberId: gitHubUserInfo?.login,
    profileImgUrl: gitHubUserInfo?.avatar_url,
    regions: userRegions,
  });
  const [isReadyToSubmit, setIsReadyToSubmit] = useState(false);
  const [idExists, setIdExists] = useState(false);
  const timerRef = useRef<ReturnType<typeof setTimeout> | undefined>(undefined);
  const { join } = useJoin();

  const handleInputChange = ({ target }: ChangeEvent<HTMLInputElement>) => {
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

  const handleUserRegions = (regions: RegionInfo[]) => {
    setUserRegions(regions);
    setUserAccount({
      ...userAccount,
      memberId: userInputId,
      regions: regions,
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
    } else {
      setValidationMessage('사용 가능한 아이디예요');
      if (userRegions.length > 0) {
        setUserAccount({
          ...userAccount,
          memberId: userInputId,
          regions: userRegions,
        });
        return setIsReadyToSubmit(true);
      }
    }
    setIsReadyToSubmit(false);
  }, [userInputId, idExists]);

  return (
    <MyJoin>
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
      <MyUserAccount>
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
        <SettingRegionSelector onSetRegions={handleUserRegions} />
      </MyUserAccount>
    </MyJoin>
  );
};

const MyJoin = styled.div`
  background-color: white;
`;

const MyUserAccount = styled.div`
  padding: 5vh 2.7vw;
`;

const MyUserInfo = styled.div`
  height: 150px;
  margin-bottom: 20px;
  > div {
    padding-bottom: 10px;
  }
`;

export default Join;
