import { useEffect, useMemo, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';

import Icon from '@assets/Icon';
import Alert from '@common/Alert';
import {
  ALERT_ACTIONS,
  ALERT_TITLE,
  AlertActionsProps,
} from '@common/Alert/constants';
import Button from '@common/Button';
import NavBar from '@common/NavBar';
import PopupSheet from '@common/PopupSheet';
import {
  DETAIL_STATUS_MENU,
  DETAIL_VIEWMORE_MENU,
} from '@common/PopupSheet/constants';
import SubTabBar from '@common/TabBar/SubTabBar';
import ChatRoom from '@components/chat/ChatRoom';
import { CategoryInfo } from '@components/home/category';
import Carousel from '@components/home/ItemDetail/Carousel';
import { ItemStatus } from '@components/ItemStatus';
import { useCategories } from '@components/layout/MobileLayout';
import PortalLayout from '@components/layout/PortalLayout';
import New from '@components/new/New';
import { formatNumberToSI } from '@utils/formatNumberToSI';
import { getFormattedPrice } from '@utils/formatPrice';
import getElapsedTime from '@utils/getElapsedTime';
import { getStoredValue } from '@utils/sessionStorage';

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

export interface ItemImages {
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

// TODO: Event Handler Prop prefix 'on'
const ItemDetail = ({
  id,
  categoryInfo,
  handleBackBtnClick,
}: ItemDetailProps) => {
  const isLogin = !!getStoredValue({ key: 'userInfo' });

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
  const [isChatRoomOpen, setIsChatRoomOpen] = useState(false);
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
  const itemInfoRef = useRef(undefined);
  const [onRefresh, setOnRefresh] = useState(false);
  const likeIcon = isLike ? 'fullHeart' : 'heart';

  const statusLabel = useMemo(() => {
    const statusType = {
      [ItemStatus.ON_SALE]: '판매중',
      [ItemStatus.RESERVATION]: '예약중',
      [ItemStatus.SOLD_OUT]: '판매완료',
    };

    return statusType[status];
  }, [status]);

  const categories = useCategories();
  const [isNewModalOpen, setIsNewModalOpen] = useState(false);

  const handleStatusSheet = async (status: ItemStatus) => {
    try {
      await api.patch(`/items/${id}/status`, { status: status });
    } catch (error) {
      console.error(`Failed to request: ${error}`);
    }
    setItemDetailInfo((prev) => ({ ...prev, status: status }));
  };

  const [isDeleteAlertOpen, setIsDeleteAlertOpen] = useState(false);
  const [isLoginAlertOpen, setIsLoginAlertOpen] = useState(false);

  const handleViewMoreSheet = (type: string) => {
    if (type === 'delete') {
      setIsDeleteAlertOpen(true);
    }
    if (type === 'edit') {
      setIsNewModalOpen(true);
    }
  };

  const handleAlert = (type: AlertActionsProps['id']) => {
    if (type === 'leave' || type === 'logout') return;

    const actions = {
      delete: () => handleDeleteAlert(type),
      cancel: () => setIsDeleteAlertOpen(false),
      home: () => handleLoginAlert(type),
      login: () => handleLoginAlert(type),
    };

    return actions[type]();
  };

  const handleDeleteAlert = (type: string) => {
    if (type === 'delete') {
      deleteItem();
      setIsDeleteAlertOpen(false);
    } else {
      setIsDeleteAlertOpen(false);
    }
  };

  // TODO: 채팅하기 버튼에도 적용하기
  const handleLoginAlertOpen = () => setIsLoginAlertOpen(true);

  const navigator = useNavigate();

  const handleLoginAlert = (type: string) => {
    if (type === 'home') {
      handleBackBtnClick(0);
      return;
    }

    if (type === 'login') {
      navigator('/login');
    }
  };

  const deleteItem = async () => {
    try {
      await api.delete(`/items/${id}`);
      handleBackBtnClick(0);
    } catch (error) {
      console.error(`Failed to request: ${error}`);
    }
  };

  const handleLike = async () => {
    if (isLogin) {
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
    } else {
      handleLoginAlertOpen();
    }
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

  const handleChatRoom = () => setIsChatRoomOpen(!isChatRoomOpen);

  const navigate = useNavigate();

  const handleChatButton = () => {
    isMyItem ? navigate(`/chat-list/${id}`) : handleChatRoom();
  };

  const handleNewModal = () => {
    setIsNewModalOpen(!isNewModalOpen);
    if (isNewModalOpen) setOnRefresh(true);
  };

  const mapItemDetailInfo = (data: any) => {
    const categoryTitle = categoryInfo.find(
      (item) => item.id === data.category,
    );

    const mappedDetails = {
      ...data,
      price: getFormattedPrice(data.price),
      category: categoryTitle?.title,
      elapsedTime: getElapsedTime(data.createAt),
      hits: data.hits && formatNumberToSI(data.hits),
      chatCount: data.chatCount && formatNumberToSI(data.chatCount),
      likesCount: data.likesCount && formatNumberToSI(data.likesCount),
    };

    setItemDetailInfo(mappedDetails);
  };

  const alertButtons = (actions: AlertActionsProps[]) =>
    actions.map(({ id, action }) => (
      <button key={id} onClick={() => handleAlert(id)}>
        {action}
      </button>
    ));

  const getItemDetail = async () => {
    try {
      const {
        data: { data },
      } = await api.get(`/items/${id}`);
      mapItemDetailInfo(data);
      itemInfoRef.current = data;
    } catch (error) {
      console.error(`Failed to get item info: ${error}`);
    }
  };

  useEffect(() => {
    if (!onRefresh) return;
    getItemDetail();
    setOnRefresh(false);
  }, [onRefresh]);

  useEffect(() => {
    getItemDetail();
  }, []);

  return (
    <PortalLayout>
      <MyItemDetail>
        <MyNavBar
          type="transparent"
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
        <Carousel images={images} itemTitle={title} />
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
      </MyItemDetail>
      <SubTabBar icon={likeIcon} content={price} onIconClick={handleLike}>
        <Button active onClick={handleChatButton}>
          {isMyItem ? `대화 중인 채팅방 ${chatCount}` : '채팅하기'}
        </Button>
      </SubTabBar>
      {!!isChatRoomOpen && (
        <ChatRoom itemId={id} onRoomClose={handleChatRoom}></ChatRoom>
      )}
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
      {isNewModalOpen && (
        <New
          isEdit={true}
          origin={itemInfoRef?.current}
          categoryInfo={categories}
          onClick={handleNewModal}
        />
      )}
      <Alert isOpen={isDeleteAlertOpen}>
        <Alert.Title>{ALERT_TITLE.DELETE('삭제')}</Alert.Title>
        <Alert.Button>{alertButtons(ALERT_ACTIONS.DELETE)}</Alert.Button>
      </Alert>
      <Alert isOpen={isLoginAlertOpen}>
        <Alert.Title>{ALERT_TITLE.LOGIN}</Alert.Title>
        <Alert.Button>{alertButtons(ALERT_ACTIONS.LOGIN)}</Alert.Button>
      </Alert>
    </PortalLayout>
  );
};

const MyItemDetail = styled.div`
  height: calc(100vh - 83px);
  overflow: auto;
`;

const MyNavBar = styled(NavBar)`
  /* NOTE: slick의 기본 z-index 값이 1000임 */
  z-index: 10010;
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
