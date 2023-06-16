import { ReactNode } from 'react';

import { styled } from 'styled-components';

interface NavBarProps {
  left?: ReactNode;
  center?: ReactNode;
  right?: ReactNode;
  children?: ReactNode;
}

const NavBar = ({ left, center, right, children }: NavBarProps) => {
  return (
    <MyNavBar>
      <MyNavBarTitle>
        {left}
        <MyNavBarCenter>{center}</MyNavBarCenter>
        {right}
      </MyNavBarTitle>
      {children && <MyNavBarChildren>{children}</MyNavBarChildren>}
    </MyNavBar>
  );
};

const MyNavBar = styled.div`
  position: sticky;
  top: 0;
  background-color: ${({ theme }) => theme.colors.neutral.backgroundBlur};
  backdrop-filter: blur(3px);

  border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
`;

const MyNavBarTitle = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  min-height: 66px;
  padding: 10px 10px;
  ${({ theme }) => theme.fonts.body};
  color: ${({ theme }) => theme.colors.neutral.textWeak};
  /* border-radius: 10px 10px 0px 0px; */ // TODO: 팝업 애니메이션 적용시 필요함
`;

const MyNavBarCenter = styled.div`
  font-weight: 600;
  color: ${({ theme }) => theme.colors.neutral.textStrong};
`;

const MyNavBarChildren = styled.div`
  display: flex;
  justify-content: center;
  padding: 0 10px 10px;
`;

export default NavBar;
