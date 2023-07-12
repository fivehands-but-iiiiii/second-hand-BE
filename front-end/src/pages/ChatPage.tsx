import { useState } from 'react';

import NavBar from '@common/NavBar';
import ChatRoomList from '@components/chat/ChatRoomList';
import BlankPage from '@pages/BlankPage';

// NOTE: mock data는 API 연동 후 삭제 예정
const mockChatList = [
  {
    id: '1',
    userImage: 'https://picsum.photos/200/300',
    userName: 'Lily',
    lastMessageTime: '2023-06-27',
    lastMessage: '안녕하시와요안녕하시와요안녕하시와요안녕하시와요안녕하시와요',
    unreadCount: 1,
    itemImage: 'https://picsum.photos/200/300',
  },
  {
    id: '2',
    userImage: 'https://picsum.photos/200/300',
    userName: 'NANII',
    lastMessageTime: '2023-06-27',
    lastMessage: '채팅중입니다요',
    unreadCount: 1,
    itemImage: 'https://picsum.photos/200/300',
  },
  {
    id: '3',
    userImage: 'https://picsum.photos/200/300',
    userName: 'FE',
    lastMessageTime: '2023-06-27',
    lastMessage: '삽니다요',
    unreadCount: 1,
    itemImage: 'https://picsum.photos/200/300',
  },
];

const ChatPage = () => {
  const title = '채팅';
  const [chatList] = useState(mockChatList);

  // TODO: 채팅 리스트 API 연동

  return (
    <>
      <NavBar center={title} />
      {chatList.length ? (
        <ChatRoomList chatItems={chatList} />
      ) : (
        <BlankPage title={title} />
      )}
    </>
  );
};

export default ChatPage;
