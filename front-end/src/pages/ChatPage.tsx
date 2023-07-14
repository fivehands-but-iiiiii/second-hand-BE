import { useState } from 'react';

import NavBar from '@common/NavBar';
import ChatRoomList from '@components/chat/ChatRoomList';
import BlankPage from '@pages/BlankPage';

// NOTE: mock data는 API 연동 후 삭제 예정
const mockChatList = [
  {
    id: '1',
    userImage: 'http://www.pororopark.com/images/sub/circle_pororo.png',
    userName: 'Pororo',
    lastMessageTime: '2023-06-27',
    lastMessage: '안녕하시와요안녕하시와요안녕하시와요안녕하시와요안녕하시와요',
    unreadCount: 1,
    itemImage:
      'https://m.pororomall.com/web/product/big/202204/ea27d867f02fc52e6fb122653b90ca04.jpg',
  },
  {
    id: '2',
    userImage: 'http://www.pororopark.com/images/sub/circle_crong.png',
    userName: 'Crong',
    lastMessageTime: '2023-06-27',
    lastMessage: '채팅중입니다요',
    unreadCount: 1,
    itemImage:
      'https://openimage.interpark.com/goods_image_big/0/8/2/2/8658370822_l.jpg',
  },
  {
    id: '3',
    userImage: 'http://www.pororopark.com/images/sub/circle_petty.png',
    userName: 'Petty',
    lastMessageTime: '2023-06-27',
    lastMessage: '삽니다요',
    unreadCount: 4,
    itemImage:
      'https://cdn.011st.com/11dims/resize/600x600/quality/75/11src/product/2581657608/B.jpg?410000000',
  },
];

const ChatPage = () => {
  const title = '채팅';
  const [chatList] = useState(mockChatList);

  // TODO: 채팅 리스트 API 연동

  return (
    <>
      <NavBar center={title} />
      {!chatList.length ? (
        <ChatRoomList chatItems={chatList} />
      ) : (
        <BlankPage title={title} />
      )}
    </>
  );
};

export default ChatPage;
