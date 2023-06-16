import Icon from '@assets/Icon';
import Button from '@common/Button';
import TabBar from '@common/TabBar';
import Textarea from '@common/Textarea';

import { styled } from 'styled-components';

const ChatTabBar = () => (
  <TabBar>
    <Textarea type="chat"></Textarea>
    <MyChatTebBarButton icon active circle="md">
      <Icon name="arrowUp" size="xs" fill="#fff" />
    </MyChatTebBarButton>
  </TabBar>
);

const MyChatTebBarButton = styled(Button)`
  padding: 0;
  margin-left: 8px;
`;

export default ChatTabBar;
