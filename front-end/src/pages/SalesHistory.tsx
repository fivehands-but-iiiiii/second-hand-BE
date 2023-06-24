import { useParams } from 'react-router-dom';

import NavBar from '@common/NavBar/NavBar';

import BlankPage from './BlankPage';

const SalesHistory = () => {
  const { id } = useParams;
  const title = '판매 내역';
  // TODO: 로그인 Id 를 기반으로 판매내역 조회
  return (
    <>
      <NavBar center={title} />
      <BlankPage title={title} />
    </>
  );
};

export default SalesHistory;
