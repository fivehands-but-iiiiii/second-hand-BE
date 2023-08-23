import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

import NavBar from '@common/NavBar';
import ChatRoomList from '@components/chat/ChatRoomList';
import BlankPage from '@pages/BlankPage';

import api from '../api';

const ChatPage = () => {
  const { itemId } = useParams();

  const title = '채팅';
  const [page, setPage] = useState(0);
  const [chatList, setChatList] = useState([]);
  const isChatListExist = !!chatList.length;

  const buildChatListApiUrl = (page: number, itemId?: string) => {
    return `/chats?page=${page}${itemId ? `&itemId=${itemId}` : ''}`;
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
