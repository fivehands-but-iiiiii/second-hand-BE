import { ReactNode } from 'react';

import { styled } from 'styled-components';

interface NavBarProps {
  type?: 'default' | 'blur' | 'transparent';
  left?: ReactNode;
  center?: ReactNode;
  right?: ReactNode;
  className?: string;
  children?: ReactNode;
}

const NavBar = ({
  type = 'default',
  left,
  center,
  right,
  className,
  children,
}: NavBarProps) => {
  const navBarTypes = {
    default: MyDefaultNavBar,
    blur: MyBlurNavBar,
    transparent: MyTransparentNavBar,
  };
  const MyNavBar = navBarTypes[type];

  return (
    <MyNavBar className={className}>
      <MyNavBarTitle>
        {left && <MyLeftTitle>{left}</MyLeftTitle>}
        <MyCenter>{center}</MyCenter>
        {right && <MyRightTitle>{right}</MyRightTitle>}
      </MyNavBarTitle>
      {children && <MyChildren>{children}</MyChildren>}
    </MyNavBar>
  );
};

const MyDefaultNavBar = styled.div`
  width: 100%;
  min-height: 70px;
  border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
`;

const MyBlurNavBar = styled(MyDefaultNavBar)`
  position: sticky;
  top: 0;
  background-color: ${({ theme }) => theme.colors.neutral.backgroundBlur};
  backdrop-filter: blur(3px);
`;

const MyTransparentNavBar = styled(MyDefaultNavBar)`
  position: fixed;
  border: none;
`;

const MyNavBarTitle = styled.div`
  display: grid;
  align-items: flex-end;
  grid-template-columns: 1fr 1fr 1fr;
  height: 100%;
  padding: 10px 10px;
  ${({ theme }) => theme.fonts.body};
`;

const MyLeftTitle = styled.p`
  text-align: left;
  grid-column: 1;
`;

const MyRightTitle = styled.p`
  text-align: right;
  grid-column: 3;
`;

const MyCenter = styled.p`
  min-width: 80px;
  font-weight: 600;
  color: ${({ theme }) => theme.colors.neutral.textStrong};
  text-align: center;
  grid-column: 2;
`;

const MyChildren = styled.div`
  display: flex;
  justify-content: center;
  padding: 0 10px 10px;
`;

export default NavBar;
