import Icon from '@assets/Icon';
import Button from '@common/Button';
import NavBar from '@common/NavBar';
import SubTabBar from '@common/TabBar/SubTabBar';
import { ItemStatus } from '@components/ItemStatus';
import { formatNumberToSI } from '@utils/formatNumberToSI';
import { getStoredValue } from '@utils/sessionStorage';

import { styled } from 'styled-components';

export interface UserInfo {
  id: 0;
  memberId: 'string';
  profileImgUrl: 'string';
  oauth: 'github';
  regions: [
    {
      id: 0;
      onFocus: true;
    },
  ];
}

interface ItemSeller {
  id: number;
  name: string;
}

interface ItemImages {
  id: number;
  url: string;
}
export interface ItemDetailInfo {
  images: ItemImages[];
  seller: ItemSeller;
  status: ItemStatus;
  title: string;
  category: number;
  createdAt: string;
  contents: string;
  chatCount: number;
  likesCount: number;
  isLike: boolean;
  hits: number;
  price: number;
}

interface ItemDetailProps {
  itemDetailInfo: ItemDetailInfo;
}

const ItemDetail = ({ itemDetailInfo }: ItemDetailProps) => {
  const {
    images,
    seller,
    status,
    title,
    category,
    createdAt,
    contents,
    chatCount,
    likesCount,
    isLike,
    hits,
    price,
  } = itemDetailInfo;
  const userInfo: UserInfo = getStoredValue({ key: 'userInfo' });
  const isMyItem = true;
  // const isMyItem = userInfo.memberId === seller.name;
  const formattedPrice = price ? `${price.toLocaleString()}원` : '가격없음';

  const statusLabel = (() => {
    switch (status) {
      case ItemStatus.ON_SALE:
        return '판매중';
      case ItemStatus.RESERVATION:
        return '예약중';
      case ItemStatus.SOLD_OUT:
        return '판매완료';
      default:
        return '';
    }
  })();

  return (
    <>
      <MyItemDetail>
        <MyNavBar
          left={<Icon name={'chevronLeft'} />}
          right={
            isMyItem && (
              <button onClick={() => console.log('open modal')}>
                <Icon name={'ellipsis'} />
              </button>
            )
          }
        />
        <MyImgDetail>
          {/* <div>이미지들..{images[0].id}</div> */}
          <MyImages>
            <img src="https://picsum.photos/203" alt={title} />
          </MyImages>
          <MyImgIcons>
            <Icon name={'selected'} size="xxs" />
            <Icon name={'selected'} size="xxs" />
            <Icon name={'selected'} size="xxs" />
          </MyImgIcons>
        </MyImgDetail>
        <MyItemInfo>
          {isMyItem || (
            <MySellerDetail>
              <div>판매자 정보</div>
              <span>{seller.name}</span>
            </MySellerDetail>
          )}
          {isMyItem && (
            <MyStatus onClick={() => console.log('open modal')}>
              <div>{statusLabel}</div>
              <Icon name={'chevronDown'} size="xs" />
            </MyStatus>
          )}
          <MyItemInfoDetail>
            <MyTitle>{title}</MyTitle>
            <MyCategoryAndTime>
              카테고리{category} &middot; {createdAt}
            </MyCategoryAndTime>
            <MyContents>{contents}</MyContents>
            <MyCountInfo>
              {!!chatCount && <div>채팅 {formatNumberToSI(chatCount)}</div>}
              {!!likesCount && (
                <div>
                  {isLike} 관심 {formatNumberToSI(likesCount)}
                </div>
              )}
              {!!hits && <div>조회 {formatNumberToSI(hits)}</div>}
            </MyCountInfo>
          </MyItemInfoDetail>
        </MyItemInfo>
      </MyItemDetail>
      <SubTabBar icon={'heart'} content={formattedPrice}>
        <Button active onClick={() => console.log('move to chat')}>
          {isMyItem ? `대화 중인 채팅방 ${chatCount}` : '채팅하기'}
        </Button>
      </SubTabBar>
    </>
  );
};

const MyItemDetail = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 83px;
`;

const MyNavBar = styled(NavBar)`
  background-color: transparent;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  border: none;
  backdrop-filter: none;
  z-index: 10;
`;

const MyImgDetail = styled.div`
  position: relative;
  display: flex;
  justify-content: center;
  width: 100%;
  height: 491px;
`;

const MyImages = styled.div`
  width: 100%;
  height: 100%;
  > img {
    object-fit: cover;
    width: 100%;
    height: 100%;
  }
`;

const MyImgIcons = styled.div`
  position: absolute;
  bottom: 0;
  display: flex;
  gap: 10px;
  margin-bottom: 18px;
`;

const MyItemInfo = styled.div`
  margin: 16px;
  display: flex;
  flex-direction: column;
  gap: 16px;
`;

const MySellerDetail = styled.div`
  display: flex;
  justify-content: space-between;
  padding: 16px;
  background-color: ${({ theme }) => theme.colors.neutral.backgroundWeak};
  color: ${({ theme }) => theme.colors.neutral.text};
  ${({ theme }) => theme.fonts.subhead};
  border-radius: 12px;
`;

const MyStatus = styled.button`
  display: flex;
  align-items: center;
  width: fit-content;
  height: 32px;
  padding: 8px 16px;
  ${({ theme }) => theme.fonts.caption1};
  border: 1px solid ${({ theme }) => theme.colors.neutral.border};
  border-radius: 8px;
  > div {
    width: 56px;
    text-align: left;
  }
`;

const MyItemInfoDetail = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
`;

const MyTitle = styled.div`
  ${({ theme }) => theme.fonts.body};
  font-weight: 590;
  text-align: left;
`;

const MyCategoryAndTime = styled.div`
  margin-top: 8px;
  ${({ theme }) => theme.fonts.footnote};
  color: ${({ theme }) => theme.colors.neutral.textWeak};
`;

const MyContents = styled.div`
  margin: 16px 0;
  ${({ theme }) => theme.fonts.body};
  color: ${({ theme }) => theme.colors.neutral.text};
  text-align: left;
`;

const MyCountInfo = styled.div`
  display: flex;
  margin-bottom: 16px;
  gap: 8px;
  color: ${({ theme }) => theme.colors.neutral.textWeak};
  ${({ theme }) => theme.fonts.footnote};
`;

export default ItemDetail;
