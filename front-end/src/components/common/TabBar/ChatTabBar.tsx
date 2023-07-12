import Icon from '@assets/Icon';
import Button from '@common/Button';
import TabBar from '@common/TabBar';
import Textarea from '@common/Textarea';

import { styled } from 'styled-components';

const ChatTabBar = () => (
  <TabBar>
    <Textarea type="chat"></Textarea>
    <MyChatTabBarButton icon active circle="md">
      <Icon name="arrowUp" size="xs" fill="#fff" />
    </MyChatTabBarButton>
  </TabBar>
);

const MyChatTabBarButton = styled(Button)`
  padding: 0;
  margin-left: 8px;
`;

export default ChatTabBar;
