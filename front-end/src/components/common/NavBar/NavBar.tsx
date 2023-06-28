import { ReactNode } from 'react';

import { styled } from 'styled-components';

interface NavBarProps {
  left?: ReactNode;
  center?: ReactNode;
  right?: ReactNode;
  className?: string;
  children?: ReactNode;
}

const NavBar = ({ left, center, right, className, children }: NavBarProps) => {
  return (
    <MyNavBar className={className}>
      <MyNavBarTitle>
        <MyLeftTitle>{left}</MyLeftTitle>
        <MyCenter>{center}</MyCenter>
        {right && <MyRightTitle>{right}</MyRightTitle>}
      </MyNavBarTitle>
      {children && <MyChildren>{children}</MyChildren>}
    </MyNavBar>
  );
};

const MyNavBar = styled.div`
  width: 100vw;
  min-height: 70px;
  position: sticky;
  top: 0;
  background-color: ${({ theme }) => theme.colors.neutral.backgroundBlur};
  backdrop-filter: blur(3px);
  border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
`;

const MyNavBarTitle = styled.div`
  display: grid;
  align-items: flex-end;
  grid-template-columns: 1fr 1fr 1fr;
  height: 10vh;
  padding: 10px 10px;
  color: ${({ theme }) => theme.colors.neutral.textWeak};
  ${({ theme }) => theme.fonts.body};
`;

const MyLeftTitle = styled.p`
  text-align: left;
`;

const MyRightTitle = styled.p`
  text-align: right;
`;

const MyCenter = styled.p`
  min-width: 80px;
  font-weight: 600;
  color: ${({ theme }) => theme.colors.neutral.textStrong};
  text-align: center;
`;

const MyChildren = styled.div`
  display: flex;
  justify-content: center;
  padding: 0 10px 10px;
`;

export default NavBar;
