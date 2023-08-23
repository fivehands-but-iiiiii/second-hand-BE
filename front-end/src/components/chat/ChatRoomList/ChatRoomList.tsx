import { useState } from 'react';
import { createPortal } from 'react-dom';

import ChatRoom from '../ChatRoom/ChatRoom';

import ChatListItem from './ChatListItem';

interface ChatRoomListProps {
  chatItems: ChatListItem[];
}

const ChatRoomList = ({ chatItems }: ChatRoomListProps) => {
  // TODO: 비지니스 로직 분리
  const [selectedItemId, setSelectedItemId] = useState(0);

  const handleChatRoomClick = (itemId: number) => {
    setSelectedItemId(itemId);
  };

  const handleChatRoom = () => setSelectedItemId(0);

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
          <ChatRoom itemId={selectedItemId} onRoomClose={handleChatRoom} />,
          document.body,
        )}
    </>
  );
};

export default ChatRoomList;
