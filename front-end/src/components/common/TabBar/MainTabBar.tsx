import { useLocation, useNavigate } from 'react-router-dom';

import Icon from '@assets/Icon';
import * as iconTypes from '@assets/svgs/index';
import Button from '@common/Button';
import palette from '@styles/colors';

import { styled } from 'styled-components';

import TabBar from './TabBar';

interface TabBarInfo {
  id: number;
  icon: keyof typeof iconTypes;
  label: string;
  path: string;
}

interface MainTabBarProps {
  userId: number;
}

interface TabBarStyleProps {
  tabColor: string;
}

const MainTabBar = ({ userId }: MainTabBarProps) => {
  // TODO: 상수는 별도의 파일로 분리 예정
  const tabBarInfo: TabBarInfo[] = [
    { id: 1, icon: 'home', label: '홈화면', path: '/' },
    {
      id: 2,
      icon: 'newspaper',
      label: '판매내역',
      path: `/items/${userId}`,
    },
    {
      id: 3,
      icon: 'heart',
      label: '관심목록',
      path: '/wishlist',
    },
    {
      id: 4,
      icon: 'message',
      label: '채팅',
      path: '/messages',
    },
    {
      id: 5,
      icon: 'person',
      label: '내 계정',
      path: '/login',
    },
  ];

  const navigate = useNavigate();
  const { pathname } = useLocation();
  const tabColor = (isClicked: boolean) => {
    return isClicked
      ? `${palette.neutral.textStrong}`
      : `${palette.neutral.textWeak}`;
  };

  return (
    <MyTabBar>
      {tabBarInfo.map(({ id, icon, label, path }) => {
        const isClicked = pathname === path;
        return (
          <Button icon fullWidth key={id} onClick={() => navigate(path)}>
            <Icon name={icon} size="md" fill={tabColor(isClicked)}></Icon>
            <MyTabLabel tabColor={tabColor(isClicked)}>{label}</MyTabLabel>
          </Button>
        );
      })}
    </MyTabBar>
  );
};

const MyTabBar = styled(TabBar)`
  padding: 11px 0 40px 0;
`;

const MyTabLabel = styled.span<TabBarStyleProps>`
  color: ${({ tabColor }) => tabColor};
  font-size: 10px;
  font-weight: 510;
`;

export default MainTabBar;
