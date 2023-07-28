import { ReactNode } from 'react';

import { styled } from 'styled-components';

interface PortalLayoutProps {
  children: ReactNode;
}

const PortalLayout = ({ children }: PortalLayoutProps) => {
  return <MyPortalLayout>{children}</MyPortalLayout>;
};

PortalLayout.Alert = ({ children }: PortalLayoutProps) => {
  return <MyAlertPortal>{children}</MyAlertPortal>;
};

const MyDefaultPortal = styled.div`
  position: absolute;
  bottom: 0;
  width: 100vw;
  height: 100vh;
  color: ${({ theme }) => theme.colors.neutral.text};
`;

const MyPortalLayout = styled(MyDefaultPortal)`
  background-color: ${({ theme }) => theme.colors.neutral.background};
  /* NOTE: slick의 기본 z-index 값이 10000임 */
  z-index: 11000;
  overflow: auto;
  -ms-overflow-style: none;
  &::-webkit-scrollbar {
    display: none;
  }
  > div {
    width: 100%;
  }
`;

const MyAlertPortal = styled(MyDefaultPortal)`
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: ${({ theme }) => theme.colors.neutral.border};
  > div {
    background-color: ${({ theme }) => theme.colors.neutral.background};
  }
`;

export default PortalLayout;
