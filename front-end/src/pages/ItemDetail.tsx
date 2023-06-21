import Icon from '@assets/Icon';
import Button from '@common/Button';
import { SaleItem } from '@common/Item';
import NavBar from '@common/NavBar';
import SubTabBar from '@common/TabBar/SubTabBar';

import { styled } from 'styled-components';

interface ItemDetailProps {
  saleItem: SaleItem;
}

const ItemDetail = ({ saleItem }: ItemDetailProps) => {
  const formattedPrice = saleItem.price
    ? `${saleItem.price.toLocaleString()}원`
    : '가격없음';
  return (
    <MyItemDetail>
      <NavBar
        left={<Icon name={'chevronLeft'} />}
        right={<Icon name={'ellipsis'} />}
      />
      <MyImgDetail>
        <div>이미지들..</div>
        <div>selected icons..</div>
      </MyImgDetail>
      <MySellerDetail>
        <span>판매자 정보</span>
        <span>판매자 아이디</span>
      </MySellerDetail>
      <MyItemInfoDetail>
        <div>아이템 이름</div>
        <div>아이템 카테고리와 작성 경과 시간</div>
        <div>아이템 설명</div>
        <div>채팅, 관심, 조회</div>
      </MyItemInfoDetail>
      <SubTabBar icon={'heart'} content={formattedPrice}>
        {/* TODO: 내가 작성한 글 ? '대화 중인 채팅방' + chat count : '채팅하기' */}
        <Button>대화 중인 채팅방</Button>
      </SubTabBar>
      <div>팝업</div>
    </MyItemDetail>
  );
};

const MyItemDetail = styled.div``;
const MyImgDetail = styled.div``;
const MySellerDetail = styled.div``;
const MyItemInfoDetail = styled.div``;

export default ItemDetail;
