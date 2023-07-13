import { createPortal } from 'react-dom';

import ChatRoom from '../ChatRoom/ChatRoom';

import ChatListItem from './ChatListItem';

interface ChatRoomListProps {
  chatItems: ChatListItem[];
}
const ChatRoomList = ({ chatItems }: ChatRoomListProps) => {
  return (
    <>
      {/* {chatItems.map((chat) => (
        <ChatListItem key={chat.id} chatItem={chat} />
      ))} */}
      {/* {createPortal(<ChatRoom itemId={1}></ChatRoom>, document.body)} */}
      <ChatRoom itemId={1}></ChatRoom>
    </>
  );
};

export default ChatRoomList;
