import { useState } from 'react';
import { Outlet } from 'react-router-dom';

import MainTabBar from '@common/TabBar/MainTabBar';

import { styled } from 'styled-components';

export interface OutletContext {
  handleMainTabBar: (status: boolean) => void;
}

const MobileLayout = () => {
  const [isMainTabBarOpen, setIsMainTabBarOpen] = useState(true);

  const handleMainTabBar = (status: boolean) => {
    setIsMainTabBarOpen(status);
  };

  return (
    <MyMobileLayout>
      <Outlet context={handleMainTabBar} />
      {isMainTabBarOpen && <MainTabBar userId={1} />}
    </MyMobileLayout>
  );
};

const MyMobileLayout = styled.div`
  width: 100vw;
  height: 100vh;
  min-width: 400px;
  background-color: #fff;
  text-align: center;
  overflow: auto;
`;

export default MobileLayout;
