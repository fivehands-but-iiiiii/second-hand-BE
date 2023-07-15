import { useState, ChangeEvent, useRef, useEffect, useMemo } from 'react';
import { createPortal } from 'react-dom';
import { useLocation, useNavigate } from 'react-router-dom';

import Icon from '@assets/Icon';
import Button from '@common/Button';
import LabelInput from '@common/LabelInput';
import NavBar from '@common/NavBar';
import SearchRegions from '@components/region/SearchRegions';
import useJoin from '@hooks/useJoin';
import UserInfo from '@pages/ItemDetail';
import palette from '@styles/colors';
import { getFormattedId } from '@utils/formatText';

import { styled } from 'styled-components';

import api from '../../api';

import UserProfile from './UserProfile';
export interface UserInfo {
  memberId: string;
  profileImgUrl?: string;
  regions: RegionInfo[];
}

export interface InputFile {
  preview: string;
  file?: File;
}

interface RegionInfo {
  id: number;
  district: string;
  onFocus: boolean;
}

const Join = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const gitHubUserInfo = location.state;
  const [userInputId, setUserInputId] = useState('');
  const [validationMessage, setValidationMessage] = useState('');
  const [regionMessage, setRegionMessage] = useState('');
  const [files, setFiles] = useState<InputFile>();
  const [selectedRegions, setSelectedRegions] = useState<RegionInfo[]>([]);
  const [userAccount, setUserAccount] = useState<UserInfo>({
    memberId: gitHubUserInfo?.login,
    profileImgUrl: gitHubUserInfo?.avatar_url,
    regions: selectedRegions,
  });
  const [isReadyToSubmit, setIsReadyToSubmit] = useState(false);
  const [idExists, setIdExists] = useState(false);
  const [isSettingRegionsModalOpen, setIsSettingRegionsModalOpen] =
    useState(false);
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

  const handleRegionModal = () => {
    setIsSettingRegionsModalOpen((prev) => !prev);
  };

  const handleSelectRegion = (id: number, district: string) => {
    if (selectedRegions.some((region) => region.id === id)) return;
    const newRegion = { id, district, onFocus: true };
    setSelectedRegions((prev) => [
      ...prev.map((region) =>
        region.onFocus ? { ...region, onFocus: false } : region,
      ),
      newRegion,
    ]);
    setIsSettingRegionsModalOpen(false);
  };

  const handleRemoveRegion = (id: number) => {
    if (selectedRegions.length === 1) {
      setRegionMessage('최소 1개의 지역을 선택해야해요');
      return;
    }
    setSelectedRegions((prev) =>
      prev
        .filter((region) => region.id !== +id)
        .map((region) => ({
          ...region,
          onFocus: true,
        })),
    );
  };

  const handleSwitchRegion = (id: number) => {
    setSelectedRegions((prev) =>
      prev.map((region) =>
        region.id === +id
          ? { ...region, onFocus: true }
          : { ...region, onFocus: false },
      ),
    );
  };

  const regionButtons = useMemo(() => {
    const selectedRegionButtons = selectedRegions.map(
      ({ id, district, onFocus }) => {
        const isActive = selectedRegions.length === 1 ? true : onFocus;
        return (
          <Button
            key={id}
            fullWidth
            active={isActive}
            onClick={() => handleSwitchRegion(id)}
          >
            {district}
            <Icon
              name={'x'}
              size={'xs'}
              fill={palette.neutral.background}
              onClick={() => handleRemoveRegion(id)}
            />
          </Button>
        );
      },
    );
    const addButton = (
      <Button fullWidth onClick={handleRegionModal}>
        <Icon name={'plus'} size={'xs'} />
        위치추가
      </Button>
    );
    return selectedRegions.length < 2
      ? [...selectedRegionButtons, addButton]
      : selectedRegionButtons;
  }, [selectedRegions]);

  useEffect(() => {
    if (selectedRegions.length < 2) {
      setRegionMessage('최소 1개 이상 최대 2개까지 선택 가능해요');
    } else setRegionMessage('');
  }, [selectedRegions.length]);

  useEffect(() => {
    if (userInputId.length < 3) {
      setValidationMessage('');
    } else if (userInputId.length >= 3 && userInputId.length < 6) {
      setValidationMessage('6~12자 이내로 입력하세요');
    } else if (idExists) {
      setValidationMessage('이미 사용중인 아이디예요');
    } else {
      setValidationMessage('사용 가능한 아이디예요');
      if (selectedRegions.length > 0) {
        setUserAccount({
          ...userAccount,
          memberId: userInputId,
          regions: selectedRegions,
        });
        return setIsReadyToSubmit(true);
      }
    }
    setIsReadyToSubmit(false);
  }, [userInputId, idExists, selectedRegions]);

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
        <div>
          <MyRegionButton>{regionButtons}</MyRegionButton>
          <MyRegionMessage>{regionMessage}</MyRegionMessage>
        </div>
        {isSettingRegionsModalOpen &&
          createPortal(
            <SearchRegions
              onPortal={handleRegionModal}
              handleSelectRegion={handleSelectRegion}
            />,
            document.body,
          )}
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

const MyRegionButton = styled.div`
  display: flex;
  gap: 7px;
`;

const MyRegionMessage = styled.p`
  color: ${({ theme }) => theme.colors.neutral.textWeak};
  ${({ theme }) => theme.fonts.caption2};
  text-align: end;
`;

export default Join;
