import { useParams } from 'react-router-dom';

import NavBar from '@common/NavBar';
import BlankPage from '@pages/BlankPage';

const ChatList = () => {
  const { id } = useParams;
  const title = '채팅';
  // TODO: 로그인 Id 를 채팅목록 조회
  return (
    <>
      <NavBar center={title} />
      <BlankPage title={title} />
    </>
  );
};

export default ChatList;
