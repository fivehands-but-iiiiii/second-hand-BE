import { ReactNode } from 'react';

import { styled } from 'styled-components';

interface PortalLayoutProps {
  children: ReactNode;
}

const PortalLayout = ({ children }: PortalLayoutProps) => {
  return <MyPortalLayout>{children}</MyPortalLayout>;
};

const MyPortalLayout = styled.div`
  position: absolute;
  bottom: 0;
  width: 100vw;
  height: 100vh;
  background-color: ${({ theme }) => theme.colors.neutral.background};
  color: ${({ theme }) => theme.colors.neutral.text};
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

export default PortalLayout;
