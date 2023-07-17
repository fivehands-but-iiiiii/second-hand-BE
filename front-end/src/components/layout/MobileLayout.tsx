import { Outlet, useOutletContext } from 'react-router-dom';

import MainTabBar from '@common/TabBar/MainTabBar';
import { UserInfo } from '@components/login/Join';
import { getStoredValue } from '@utils/sessionStorage';

import { styled } from 'styled-components';
export interface OutletContext {
  handleMainTabBar: (status: boolean) => void;
}

type ContextType = { userInfo: UserInfo | null };

export function useUserInfo() {
  return useOutletContext<ContextType>();
}

const MobileLayout = () => {
  const userInfo = getStoredValue({ key: 'userInfo' });

  return (
    <MyMobileLayout>
      <Outlet context={{ userInfo }} />
      <MainTabBar />
    </MyMobileLayout>
  );
};

const MyMobileLayout = styled.div`
  width: 100vw;
  height: 100vh;
  background-color: #fff;
  text-align: center;
  overflow: auto;
  -ms-overflow-style: none;
  &::-webkit-scrollbar {
    display: none;
  }
`;

export default MobileLayout;
