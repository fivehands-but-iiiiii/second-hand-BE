import { ComponentPropsWithRef } from 'react';

import { styled } from 'styled-components';

interface ChatListItem extends ComponentPropsWithRef<'button'> {
  userImage: string;
  userName: string;
  lastMessageTime: string;
  lastMessage: string;
  unreadCount: number;
  itemImage: string;
}

const ChatListItem = ({
  userImage,
  userName,
  lastMessageTime,
  lastMessage,
  unreadCount,
  itemImage,
}: ChatListItem) => {
  return (
    <MyChatListItem>
      <div>{userImage}</div>
      <MyChatInfo>
        <MyChatUserName>
          <div>{userName}</div>
          <div>{lastMessageTime}</div>
        </MyChatUserName>
        <MyChatLastMessage>{lastMessage}</MyChatLastMessage>
      </MyChatInfo>
      <div>{unreadCount}</div>
      <div>{itemImage}</div>
    </MyChatListItem>
  );
};

const MyChatListItem = styled.button`
  width: 100%;
  min-width: 393px;
  display: flex;
  align-items: center;
  padding: 16px;
  gap: 8px;
  border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
  > div {
    display: flex;
  }
`;

const MyChatInfo = styled.div`
  min-width: 240px;
  flex-direction: column;
  flex-grow: 1;
  align-items: flex-start;
  gap: 4px;
`;

const MyChatUserName = styled.div`
  display: flex;
  gap: 4px;
  color: ${({ theme }) => theme.colors.neutral.textStrong};
`;

const MyChatLastMessage = styled.div`
  width: 100%;
  color: ${({ theme }) => theme.colors.neutral.text};
  ${({ theme }) => theme.fonts.footnote};
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

export default ChatListItem;
