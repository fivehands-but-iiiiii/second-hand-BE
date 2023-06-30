import { useEffect, useMemo, useState } from 'react';

import Icon from '@assets/Icon';
import Button from '@common/Button';
import NavBar from '@common/NavBar';
import {
  DETAIL_STATUS_MENU,
  DETAIL_VIEWMORE_MENU,
} from '@common/PopupSheet/constants';
import PopupSheet from '@common/PopupSheet/PopupSheet';
import SubTabBar from '@common/TabBar/SubTabBar';
import { CategoryInfo } from '@components/home/category';
import { ItemStatus } from '@components/ItemStatus';
import PortalLayout from '@components/layout/PortalLayout';
import { formatNumberToSI } from '@utils/formatNumberToSI';
import getElapsedTime from '@utils/getElapsedTime';

import { styled } from 'styled-components';

import api from '../api';

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
  memberId: string;
}

interface ItemImages {
  order: number;
  url: string;
}

export interface ItemDetailInfo {
  id: number;
  seller: ItemSeller;
  images: ItemImages[];
  status: ItemStatus;
  title: string;
  category: number;
  elapsedTime: string;
  contents: string;
  chatCount: number;
  likesCount: number;
  isLike: boolean;
  hits: number;
  price: string;
  isMyItem: boolean;
}

interface ItemDetailProps {
  id: number;
  categoryInfo: CategoryInfo[];
  handleBackBtnClick: (id: number) => void;
}

const ItemDetail = ({
  id,
  categoryInfo,
  handleBackBtnClick,
}: ItemDetailProps) => {
  const [itemDetailInfo, setItemDetailInfo] = useState<ItemDetailInfo>({
    id: 0,
    seller: { id: 0, memberId: '' },
    images: [],
    status: 0,
    title: '',
    category: 0,
    elapsedTime: '',
    contents: '',
    chatCount: 0,
    likesCount: 0,
    isLike: false,
    hits: 0,
    price: '',
    isMyItem: false,
  });
  const [isStatusPopupOpen, setIsStatusPopupOpen] = useState(false);
  const [isMoreViewPopupOpen, setIsMoreViewPopupOpen] = useState(false);
  const {
    seller,
    images,
    status,
    title,
    category,
    elapsedTime,
    contents,
    chatCount,
    likesCount,
    isLike,
    hits,
    price,
    isMyItem,
  } = itemDetailInfo;
  const likeIcon = isLike ? 'fullHeart' : 'heart';

  const statusLabel = useMemo(() => {
    const statusType = {
      [ItemStatus.ON_SALE]: '판매중',
      [ItemStatus.RESERVATION]: '예약중',
      [ItemStatus.SOLD_OUT]: '판매완료',
    };

    return statusType[status];
  }, [status]);

  const handleStatusSheet = async (status: ItemStatus) => {
    try {
      await api.patch(`/items/${id}/status`, { status: status });
    } catch (error) {
      console.error(`Failed to request: ${error}`);
    }
    setItemDetailInfo((prev) => ({ ...prev, status: status }));
  };

  const handleViewMoreSheet = async (type: string) => {
    if (type === 'delete') {
      try {
        await api.delete(`/items/${id}`);
      } catch (error) {
        console.error(`Failed to request: ${error}`);
      }
    } else if (type === 'edit') {
      // TODO: 수정 페이지로 이동!
    }
  };

  const handleLike = async () => {
    let likesCount = itemDetailInfo.likesCount;

    if (isLike) {
      try {
        await api.delete(`/wishlist/like?itemId=${id}`);
        likesCount--;
      } catch (error) {
        console.error(`Failed to request: ${error}`);
      }
    } else {
      try {
        await api.post('/wishlist/like', { itemId: id });
        likesCount++;
      } catch (error) {
        console.error(`Failed to request: ${error}`);
      }
    }

    setItemDetailInfo((prev) => ({
      ...prev,
      isLike: !prev.isLike,
      likesCount: likesCount,
    }));
  };

  const statusPopupSheetMenu = DETAIL_STATUS_MENU.filter(
    (menu) => menu.id !== status,
  ).map((menu) => ({
    ...menu,
    onClick: () => {
      handleStatusSheet(menu.id);
      setIsStatusPopupOpen(false);
    },
  }));

  const viewMorePopupSheetMenu = DETAIL_VIEWMORE_MENU.map((menu) => ({
    ...menu,
    onClick: () => {
      handleViewMoreSheet(menu.id);
      setIsMoreViewPopupOpen(false);
    },
  }));

  const handleStatusPopup = () => setIsStatusPopupOpen(!isStatusPopupOpen);

  const handleViewMorePopup = () =>
    setIsMoreViewPopupOpen(!isMoreViewPopupOpen);

  const mapItemDetailInfo = (data: any) => {
    const formattedPrice = data.price
      ? `${data.price.toLocaleString()}원`
      : '가격없음';

    const categoryTitle = categoryInfo.find(
      (item) => item.id === data.category,
    );

    const mappedDetails = {
      ...data,
      price: formattedPrice,
      category: categoryTitle?.title,
      elapsedTime: getElapsedTime(data.createAt),
      hits: hits && formatNumberToSI(data.hits),
      chatCount: chatCount && formatNumberToSI(data.chatCount),
      likesCount: likesCount && formatNumberToSI(data.likesCount),
    };

    setItemDetailInfo(mappedDetails);
  };

  useEffect(() => {
    const getItemDetail = async () => {
      try {
        const {
          data: { data },
        } = await api.get(`/items/${id}`);

        mapItemDetailInfo(data);
      } catch (error) {
        console.error(`Failed to get item info: ${error}`);
      }
    };

    getItemDetail();
  }, []);

  return (
    <PortalLayout>
      <div>
        <NavBar
          left={
            <button onClick={() => handleBackBtnClick(0)}>
              <Icon name={'chevronLeft'} />
            </button>
          }
          right={
            isMyItem && (
              <button onClick={handleViewMorePopup}>
                <Icon name={'ellipsis'} />
              </button>
            )
          }
        />
        <MyImgDetail>
          <MyImages>
            {images.map((image) => (
              <img key={image.order} src={image.url} alt={title} />
            ))}
          </MyImages>
          {images.length > 1 && (
            <MyImgIcons>
              {images.map((image) => (
                <Icon key={image.order} name={'selected'} size="xxs" />
              ))}
            </MyImgIcons>
          )}
        </MyImgDetail>
        <MyItemInfo>
          {isMyItem || (
            <MySellerDetail>
              <div>판매자 정보</div>
              <span>{seller.memberId}</span>
            </MySellerDetail>
          )}
          {isMyItem && (
            <MyStatus onClick={handleStatusPopup}>
              <div>{statusLabel}</div>
              <Icon name={'chevronDown'} size="xs" />
            </MyStatus>
          )}
          <MyItemInfoDetail>
            <MyTitle>{title}</MyTitle>
            <MyCategoryAndTime>
              {category} &middot; {elapsedTime}
            </MyCategoryAndTime>
            <MyContents>{contents}</MyContents>
            <MyCountInfo>
              {!!chatCount && <div>채팅 {formatNumberToSI(chatCount)}</div>}
              {!!likesCount && <div>관심 {formatNumberToSI(likesCount)}</div>}
              {!!hits && <div>조회 {formatNumberToSI(hits)}</div>}
            </MyCountInfo>
          </MyItemInfoDetail>
        </MyItemInfo>
      </div>
      <SubTabBar icon={likeIcon} content={price} onIconClick={handleLike}>
        <Button active onClick={() => console.log('move to chat')}>
          {isMyItem ? `대화 중인 채팅방 ${chatCount}` : '채팅하기'}
        </Button>
      </SubTabBar>
      {isStatusPopupOpen && (
        <PopupSheet
          type={'slideUp'}
          menu={statusPopupSheetMenu}
          onSheetClose={handleStatusPopup}
        ></PopupSheet>
      )}
      {isMoreViewPopupOpen && (
        <PopupSheet
          type={'slideUp'}
          menu={viewMorePopupSheetMenu}
          onSheetClose={handleViewMorePopup}
        ></PopupSheet>
      )}
    </PortalLayout>
  );
};

const MyImgDetail = styled.div`
  display: flex;
  justify-content: center;
  width: 100%;
  height: 491px;
`;

const MyImages = styled.div`
  /* position: relative; */
  overflow: hidden;
  width: 100%;
  > img {
    height: 100%;
    width: 100%;
    object-fit: cover;
  }
`;

const MyImgIcons = styled.div`
  position: absolute;
  /* bottom: 0; */
  top: 546px;
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
  margin-bottom: 75px;
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
