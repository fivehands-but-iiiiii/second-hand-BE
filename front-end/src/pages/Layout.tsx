import { Outlet } from 'react-router-dom';

import MainTabBar from '@common/TabBar/MainTabBar';

import { styled } from 'styled-components';

const MobileLayout = () => {
  return (
    <MyMobileLayout>
      <Outlet />
      <MainTabBar userId={1} />
    </MyMobileLayout>
  );
};

const MyMobileLayout = styled.div`
  width: 100vw;
  height: 100vh;
  background-color: #fff;
  text-align: center;
  overflow: auto;
`;

export default MobileLayout;
