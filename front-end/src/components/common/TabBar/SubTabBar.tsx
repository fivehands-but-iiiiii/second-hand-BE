import { ReactNode } from 'react';

import Icon from '@assets/Icon';
import * as iconTypes from '@assets/svgs/index';
import Button from '@common/Button';
import TabBar from '@common/TabBar';

import { styled } from 'styled-components';

interface SubTabBarProps {
  icon: keyof typeof iconTypes;
  content: string;
  children: ReactNode;
  onIconClick?: () => void;
}

const SubTabBar = ({
  icon,
  content,
  children,
  onIconClick,
}: SubTabBarProps) => (
  <TabBar>
    <MyTabBarIconButton icon>
      <Icon name={icon} size="lg" onClick={onIconClick} />
      <span>{content}</span>
    </MyTabBarIconButton>
    <div>{children}</div>
  </TabBar>
);

const MyTabBarIconButton = styled(Button)`
  flex-direction: row;
  padding: 0;
  ${({ theme }) => theme.fonts.footnote}
`;

export default SubTabBar;
