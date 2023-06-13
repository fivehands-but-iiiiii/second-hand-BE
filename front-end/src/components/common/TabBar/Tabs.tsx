import Icon from '@assets/Icon';
import * as iconTypes from '@assets/svgs/index';

import { styled } from 'styled-components';

import Button from '../Button/Button';

interface TabsInfo {
  id: number;
  icon: keyof typeof iconTypes;
  text: string;
  isClicked: boolean;
}

const Tabs = () => {
  // TODO: 별도의 파일로 분리 예정
  const tabsInfo: TabsInfo[] = [
    { id: 1, icon: 'home', text: '홈화면', isClicked: true },
    { id: 2, icon: 'newspaper', text: '판매내역', isClicked: false },
    { id: 3, icon: 'heart', text: '관심목록', isClicked: false },
    { id: 4, icon: 'message', text: '채팅', isClicked: false },
    { id: 5, icon: 'person', text: '내 계정', isClicked: false },
  ];

  return (
    <MyTabs>
      {tabsInfo.map((tabInfo) => (
        <Button icon fullWidth key={tabInfo.id}>
          <Icon name={tabInfo.icon} size="md"></Icon>
          <span>{tabInfo.text}</span>
        </Button>
      ))}
    </MyTabs>
  );
};

const MyTabs = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  & > button {
    font-size: 10px;
    font-weight: 510;
    color: ${({ theme }) => theme.colors.neutral.textWeak};
  }
`;

export default Tabs;
