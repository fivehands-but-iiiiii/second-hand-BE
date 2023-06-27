// import { useParams } from 'react-router-dom';

import { useEffect, useState } from 'react';

import NavBar from '@common/NavBar';
import BlankPage from '@pages/BlankPage';
import api from 'api';

const ChatList = () => {
  // const { id } = useParams;
  const title = '채팅';
  const [chatList, setChatList] = useState([]);

  // useEffect(() => {
  //   api.get(`/items/${id}`).then((res) => {
  //     setChatList(res.data);
  //   });
  // }, []);

  return (
    <>
      <NavBar center={title} />
      <BlankPage title={title} />
    </>
  );
};

export default ChatList;
