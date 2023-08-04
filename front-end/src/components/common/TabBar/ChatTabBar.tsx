import { ChangeEvent } from 'react';

import Icon from '@assets/Icon';
import Button from '@common/Button';
import TabBar from '@common/TabBar';
import Textarea from '@common/Textarea';

import { styled } from 'styled-components';

interface ChatTabBarProps {
  chatInput: string;
  handleInputChange: (event: ChangeEvent<HTMLTextAreaElement>) => void;
  handleChatSubmit: (chat: string) => void;
}

const ChatTabBar = ({
  chatInput,
  handleInputChange,
  handleChatSubmit,
}: ChatTabBarProps) => {
  // TODO: 더 나은 함수명으로 ...
  const _handleChatSubmit = () => handleChatSubmit(chatInput);

  return (
    <TabBar>
      <Textarea
        type="chat"
        rows={4}
        value={chatInput}
        autoFocus
        onChange={handleInputChange}
        onKeyDown={_handleChatSubmit}
      ></Textarea>
      <MyChatTabBarButton icon active circle="md" onClick={_handleChatSubmit}>
        <Icon name="arrowUp" size="xs" fill="#fff" />
      </MyChatTabBarButton>
    </TabBar>
  );
};

const MyChatTabBarButton = styled(Button)`
  padding: 0;
  margin-left: 8px;
`;

export default ChatTabBar;
