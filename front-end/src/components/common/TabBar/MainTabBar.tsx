import { NavLink } from 'react-router-dom';

import Icon from '@assets/Icon';
import * as iconTypes from '@assets/svgs/index';

import { css, styled } from 'styled-components';

import TabBar from './TabBar';

interface TabBarInfo {
  id: number;
  icon: keyof typeof iconTypes;
  label: string;
  path: string;
}

interface IconStyleProps {
  name: keyof typeof iconTypes;
  size?: string;
  fill?: string;
  isActive?: boolean;
}

const MainTabBar = () => {
  const tabBarInfo: TabBarInfo[] = [
    { id: 1, icon: 'home', label: '홈화면', path: '/' },
    {
      id: 2,
      icon: 'newspaper',
      label: '판매내역',
      path: '/sales-history',
    },
    {
      id: 3,
      icon: 'heart',
      label: '관심목록',
      path: '/wish-list',
    },
    {
      id: 4,
      icon: 'message',
      label: '채팅',
      path: 'chat-list',
    },
    {
      id: 5,
      icon: 'person',
      label: '내 계정',
      path: '/login',
    },
  ];

  return (
    <MyTabBar>
      {tabBarInfo.map(({ id, icon, label, path }) => (
        <MyNavLink key={id} to={path}>
          {({ isActive }: { isActive: boolean }) => (
            <>
              <MyIcon name={icon} size="md" isActive={isActive} />
              <MyTabLabel>{label}</MyTabLabel>
            </>
          )}
        </MyNavLink>
      ))}
    </MyTabBar>
  );
};

const MyTabBar = styled(TabBar)`
  padding-top: 7px;
`;

const MyNavLink = styled(NavLink)`
  min-width: 50px;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 4px;
  color: ${({ theme }) => theme.colors.neutral.textWeak};
  gap: 7px;

  &.active {
    color: ${({ theme }) => theme.colors.neutral.textStrong};
  }
`;

const MyIcon = styled(Icon)<IconStyleProps>`
  ${({ isActive, theme }) =>
    isActive
      ? css`
          fill: ${theme.colors.neutral.textStrong};
        `
      : css`
          fill: ${theme.colors.neutral.textWeak};
        `}
`;

const MyTabLabel = styled.span`
  color: inherit;
  font-size: 10px;
  font-weight: 510;
`;

export default MainTabBar;
