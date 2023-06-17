import { Outlet } from 'react-router-dom';

import NavBar from '@common/NavBar';
import MainTabBar from '@common/TabBar/MainTabBar';

import { styled } from 'styled-components';

const MobileLayout = () => {
  return (
    <MyMobileLayout>
      <NavBar>this is header!</NavBar>
      <Outlet />
      <MainTabBar userId={1} />
    </MyMobileLayout>
  );
};

const MyMobileLayout = styled.div`
  position: relative;
  width: 393px;
  height: 852px;
  margin: 0 auto;
  background-color: #fff;
  overflow: auto;
`;

export default MobileLayout;
