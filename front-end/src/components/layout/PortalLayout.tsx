import { ReactNode } from 'react';

import { styled, keyframes } from 'styled-components';

interface PortalLayoutProps {
  children: ReactNode;
}

const PortalLayout = ({ children }: PortalLayoutProps) => {
  return (
    <>
      <MyPortalBackground />
      <MyPortalLayout>{children}</MyPortalLayout>
    </>
  );
};

const slideInAnimation = keyframes`
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
`;

const MyPortalBackground = styled.div`
  position: absolute;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  min-width: 400px;
  background-color: ${({ theme }) => theme.colors.neutral.textStrong};
  animation: ${slideInAnimation} 0.3s ease-in-out;
`;

const MyPortalLayout = styled.div`
  width: 100vw;
  height: 100vh;
  min-width: 400px;
  background-color: #fff;
  text-align: center;
`;

// const slideOutAnimation = keyframes`
//   from {
//     transform: translateY(0);
//   }
//   to {
//     transform: translateY(100%);
//   }
// `;

export default PortalLayout;
