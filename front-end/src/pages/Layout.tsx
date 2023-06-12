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
  width: 393px;
  height: 852px;
  margin: 0 auto;
  background-color: #fff;
`;

export default MobileLayout;
