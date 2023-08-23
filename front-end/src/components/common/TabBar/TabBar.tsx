import { ReactNode } from 'react';

import { styled } from 'styled-components';

interface TabBarProps {
  className?: string;
  children: ReactNode;
}

// TODO: component composition로 구현하는 게 어떨지 고민해보기
const TabBar = ({ className, children }: TabBarProps) => {
  return <MyTabBar className={className}>{children}</MyTabBar>;
};

const MyTabBar = styled.div`
  display: flex;
  position: fixed;
  bottom: 0;
  width: 100%;
  height: 83px;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  padding: 11px 16px 40px 16px;
  background-color: ${({ theme }) => theme.colors.neutral.backgroundWeak};
  border-top: 1px solid ${({ theme }) => theme.colors.neutral.border};
`;

export default TabBar;
