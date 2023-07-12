import { ComponentPropsWithRef } from 'react';

import Button from '@common/Button/Button';
import ImgBox from '@common/ImgBox/ImgBox';
import UserProfile from '@components/login/UserProfile';

import { styled } from 'styled-components';

interface ChatListItem extends ComponentPropsWithRef<'button'> {
  id: string;
  userImage: string;
  userName: string;
  lastMessageTime: string;
  lastMessage: string;
  unreadCount: number;
  itemImage: string;
}

interface ChatListProps {
  chatItem: ChatListItem;
}

const ChatListItem = ({ chatItem }: ChatListProps) => {
  const {
    userImage,
    userName,
    lastMessageTime,
    lastMessage,
    unreadCount,
    itemImage,
  } = chatItem;
  return (
    <MyChatListItem>
      <UserProfile size="s" profileImgUrl={userImage} />
      <MyChatInfo>
        <MyChatUserName>
          <div>{userName}</div>
          <div>{lastMessageTime}</div>
        </MyChatUserName>
        <MyChatLastMessage>{lastMessage}</MyChatLastMessage>
      </MyChatInfo>
      <MyChatItem>
        <Button active circle="sm">
          {unreadCount}
        </Button>
        <ImgBox src={itemImage} size="sm" alt={itemImage} />
      </MyChatItem>
    </MyChatListItem>
  );
};

const MyChatListItem = styled.button`
  width: 100%;
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
  min-width: 180px;
  flex-direction: column;
  flex-grow: 1;
  align-items: flex-start;
  gap: 4px;
`;

const MyChatUserName = styled.div`
  display: flex;
  gap: 4px;
  color: ${({ theme }) => theme.colors.neutral.textStrong};
  ${({ theme }) => theme.fonts.subhead};
  > div:nth-child(2) {
    color: ${({ theme }) => theme.colors.neutral.textWeak};
    ${({ theme }) => theme.fonts.footnote};
  }
`;

const MyChatLastMessage = styled.div`
  width: 100%;
  color: ${({ theme }) => theme.colors.neutral.text};
  ${({ theme }) => theme.fonts.footnote};
  text-align: start;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

const MyChatItem = styled.div`
  display: flex;
  gap: 8px;
`;

export default ChatListItem;
