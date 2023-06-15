import { Outlet } from 'react-router-dom';

import styled from 'styled-components';

const MobileLayout = () => {
  return (
    <MyMobileLayout>
      <Outlet />
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
