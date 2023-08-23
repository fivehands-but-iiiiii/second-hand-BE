import { ReactNode } from 'react';
import { createPortal } from 'react-dom';

import { styled } from 'styled-components';

interface PortalLayoutProps {
  children: ReactNode;
}

const PortalLayout = ({ children }: PortalLayoutProps) => {
  return createPortal(
    <MyPortalLayout>{children}</MyPortalLayout>,
    document.body,
  );
};

PortalLayout.Alert = ({ children }: PortalLayoutProps) => {
  return createPortal(<MyAlertPortal>{children}</MyAlertPortal>, document.body);
};

const MyDefaultPortal = styled.div`
  z-index: 11000;
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100vw;
  height: 100vh;
  color: ${({ theme }) => theme.colors.neutral.text};
`;

const MyPortalLayout = styled(MyDefaultPortal)`
  background-color: ${({ theme }) => theme.colors.neutral.background};
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
