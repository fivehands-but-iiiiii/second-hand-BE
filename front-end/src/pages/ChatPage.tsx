import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

import NavBar from '@common/NavBar';
import ChatRoomList from '@components/chat/ChatRoomList';
import BlankPage from '@pages/BlankPage';

import api from '../api';

// // NOTE: mock data는 API 연동 후 삭제 예정
// const mockChatList = [
//   {
//     id: '167',
//     userImage: 'http://www.pororopark.com/images/sub/circle_pororo.png',
//     userName: 'Pororo',
//     lastMessageTime: '2023-06-27',
//     lastMessage: '안녕하시와요안녕하시와요안녕하시와요안녕하시와요안녕하시와요',
//     unreadCount: 1,
//     itemInfo: {
//       id: 167,
//       title: '팝니다요',
//       thumbnailUrl:
//         'https://shopping-phinf.pstatic.net/main_3814615/38146153622.20230221165951.jpg?type=f640',
//     },
//   },
//   {
//     id: '2',
//     userImage: 'http://www.pororopark.com/images/sub/circle_crong.png',
//     userName: 'Crong',
//     lastMessageTime: '2023-06-27',
//     lastMessage: '채팅중입니다요',
//     unreadCount: 1,
//     itemInfo: {
//       id: 167,
//       title: '팝니다요',
//       thumbnailUrl:
//         'https://openimage.interpark.com/goods_image_big/0/8/2/2/8658370822_l.jpg',
//     },
//   },
//   {
//     id: '3',
//     userImage: 'http://www.pororopark.com/images/sub/circle_petty.png',
//     userName: 'Petty',
//     lastMessageTime: '2023-06-27',
//     lastMessage: '삽니다요',
//     unreadCount: 4,
//     itemInfo: {
//       id: 167,
//       title: '팝니다요',
//       thumbnailUrl:
//         'https://cdn.011st.com/11dims/resize/600x600/quality/75/11src/product/2581657608/B.jpg?410000000',
//     },
//   },
// ];

const ChatPage = () => {
  const { itemId } = useParams();

  const title = '채팅';
  const [page, setPage] = useState(0);
  const [chatList, setChatList] = useState([]);
  const isChatListExist = !!chatList.length;

  const buildChatListApiUrl = (page: number, itemId?: string) => {
    let url = `/chats?page=${page}`;

    if (itemId) {
      url += `&itemId=${itemId}`;
    }

    return url;
  };

  const getChatList = async () => {
    const apiUrl = buildChatListApiUrl(page, itemId);

    try {
      const { data: chatList } = await api.get(apiUrl);

      setPage(chatList.page);
      setChatList(chatList);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getChatList();
  }, []);

  return (
    <>
      <NavBar center={title} />
      {isChatListExist ? (
        <ChatRoomList chatItems={chatList} />
      ) : (
        <BlankPage title={title} />
      )}
    </>
  );
};

export default ChatPage;
