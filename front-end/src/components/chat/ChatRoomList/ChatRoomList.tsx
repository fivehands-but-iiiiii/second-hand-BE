import { useState } from 'react';
import { createPortal } from 'react-dom';

import ChatRoom from '../ChatRoom/ChatRoom';

import ChatListItem from './ChatListItem';

interface ChatRoomListProps {
  chatItems: ChatListItem[];
}

const ChatRoomList = ({ chatItems }: ChatRoomListProps) => {
  const [selectedItemId, setSelectedItemId] = useState(0);

  const handleChatRoomClick = (itemId: number) => {
    setSelectedItemId(itemId);
  };

  return (
    <>
      {chatItems.map((chat) => (
        <ChatListItem
          key={chat.id}
          chatItem={chat}
          onClick={() => handleChatRoomClick(chat.itemInfo.id)}
        />
      ))}
      {!!selectedItemId &&
        createPortal(
          <ChatRoom itemId={selectedItemId}></ChatRoom>,
          document.body,
        )}
    </>
  );
};

export default ChatRoomList;
