import { SetStateAction, useEffect, useRef, useState } from 'react';

import Icon from '@assets/Icon';
import ImgBox from '@common/ImgBox';
import NavBar from '@common/NavBar';
import PopupSheet from '@common/PopupSheet';
import { CHAT_VIEWMORE_MENU } from '@common/PopupSheet/constants';
import ChatTabBar from '@common/TabBar/ChatTabBar';
import PortalLayout from '@components/layout/PortalLayout';
import * as StompJs from '@stomp/stompjs';
import { getFormattedPrice } from '@utils/formatPrice';
import { getStoredValue } from '@utils/sessionStorage';

import { styled } from 'styled-components';

import api from '../../../api';

// TODO: API 확정 이후 타입 정의
interface ChatBubble {
  roomId: string;
  sender: string;
  // receiver: string;
  message: string;
  // createdAt: string;
}

interface SaleItemSummary {
  id: number;
  title: string;
  price: number;
  thumbnailUrl: string;
  status: number;
  isDelete: boolean;
}

interface ChatRoomProps {
  // TODO: chat list 에서 채팅방 입장 시 필요한 정보 : roomId (서버 에러 수정 후 작업 예정)
  itemId: number;
  onRoomClose: () => void;
}

const ChatRoom = ({ itemId, onRoomClose }: ChatRoomProps) => {
  const { id: userId } = getStoredValue({ key: 'userInfo' });

  const [itemInfo, setItemInfo] = useState<SaleItemSummary>(
    {} as SaleItemSummary,
  );
  const [roomId, setRoomId] = useState<Pick<ChatBubble, 'roomId'> | null>(null);
  const [page] = useState(0);
  const [opponentId, setOpponentId] = useState('');
  const [chatBubbles, setChatBubbles] = useState<ChatBubble[]>([]);
  const [chat, setChat] = useState('');
  const [isMoreViewPopupOpen, setIsMoreViewPopupOpen] = useState(false);

  const endRef = useRef<HTMLDivElement | null>(null);
  const client = useRef<StompJs.Client | null>(null);

  const getChatInfo = async (itemId: number) => {
    try {
      const { data } = await api.get(`chats/items/${itemId}`);
      const { item, chatroomId, opponentId } = data.data;

      setItemInfo({
        ...item,
        id: item.itemId,
        price: getFormattedPrice(item.price),
        status: parseInt(item.status),
        thumbnailUrl: item.thumbnailImgUrl,
      });
      setRoomId(chatroomId);
      setOpponentId(opponentId);

      if (chatroomId) {
        getChatBubbles(chatroomId, page);
      }
    } catch (error) {
      console.error(error);
    }
  };

  const createChatRoomId = async () => {
    try {
      const { data } = await api.post('/chats', {
        itemId,
      });

      setRoomId(data.data);
    } catch (error) {
      console.error(error);
    }
  };

  const getChatBubbles = async (chatroomId: number, page: number) => {
    // TODO: data: { cahtBubbles: [], hasNext: boolean, hasPrev: boolean } 데이터로 무한 스크롤 구현하기
    const { data } = await api.get(`chats/${chatroomId}/logs?page=${page}`);
    const { chatBubbles } = data.data;

    setChatBubbles(chatBubbles);
  };

  // TODO: chat list 에서 채팅방 입장 시
  // const getChatBubbles = async (chatroomId: number) => {
  //   const { data } = await api.get(`chats/${chatroomId}`);
  //   setChatBubbles(data);
  // };

  const connect = () => {
    client.current = new StompJs.Client({
      brokerURL: 'ws://3.37.51.148:81/chat',
      onConnect: () => {
        subscribe();
        chat && publish(chat);
      },
    });

    client.current.activate();
  };

  const handleMessage = (messageBody: { body: string }) => {
    const parsedMessage = JSON.parse(messageBody.body);
    setChatBubbles((prevChatList) => [...prevChatList, parsedMessage]);
  };

  const subscribe = () => {
    client.current?.subscribe(`/sub/${roomId}`, handleMessage);
  };

  const publish = (chat: string) => {
    if (!client.current?.connected) return;

    client.current.publish({
      destination: '/pub/message',
      body: JSON.stringify({
        roomId,
        sender: userId,
        // receiver: opponentId,
        message: chat,
      }),
    });

    setChat('');
  };

  const disconnect = () => {
    client.current?.deactivate();
  };

  const handleChange = (event: {
    target: { value: SetStateAction<string> };
  }) => {
    setChat(event.target.value);
  };

  const handleSubmit = async (chat: string) => {
    if (!roomId) {
      await createChatRoomId();
      connect();
    }

    publish(chat);
  };

  const handleViewMorePopup = () => {
    setIsMoreViewPopupOpen(!isMoreViewPopupOpen);
  };

  const viewMorePopupSheetMenu = CHAT_VIEWMORE_MENU.map((menu) => ({
    ...menu,
    onClick: () => {
      // TODO: popup sheet 메뉴 클릭 시 동작
      console.log('');
      setIsMoreViewPopupOpen(false);
    },
  }));

  useEffect(() => {
    getChatInfo(itemId);
  }, [itemId]);

  useEffect(() => {
    roomId && connect();

    return () => disconnect();
  }, [roomId]);

  useEffect(() => {
    endRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [chatBubbles]);

  return (
    <PortalLayout>
      <NavBar
        left={
          <MyNavBarBtn onClick={onRoomClose}>
            <Icon name={'chevronLeft'} />
            <span>뒤로</span>
          </MyNavBarBtn>
        }
        center={opponentId}
        right={
          <button onClick={handleViewMorePopup}>
            <Icon name={'ellipsis'} />
          </button>
        }
      />
      <MyChatRoomItem>
        <ImgBox src={itemInfo.thumbnailUrl} alt={itemInfo.title} size={'sm'} />
        <MyChatRoomItemInfo>
          <span>{itemInfo.title}</span>
          <span>{itemInfo.price}</span>
        </MyChatRoomItemInfo>
      </MyChatRoomItem>
      {/* TODO: MyChatBubbles component 분리 */}
      {!!chatBubbles.length && (
        <MyChatBubbles>
          {chatBubbles.map((bubble) => {
            const isMyBubble = bubble.sender === userId;
            const BubbleComponent = isMyBubble ? MyBubble : MyOpponentBubble;
            const renderBubbleComponent = (
              <BubbleComponent>
                <span>{bubble.message}</span>
              </BubbleComponent>
            );

            return isMyBubble ? (
              <>{renderBubbleComponent}</>
            ) : (
              <MyOpponentBubbleWrapper>
                {renderBubbleComponent}
              </MyOpponentBubbleWrapper>
            );
          })}
        </MyChatBubbles>
      )}
      {isMoreViewPopupOpen && (
        <PopupSheet
          type={'slideUp'}
          menu={viewMorePopupSheetMenu}
          onSheetClose={handleViewMorePopup}
        ></PopupSheet>
      )}
      <div ref={endRef}></div>
      <ChatTabBar
        chatInput={chat}
        handleInputChange={handleChange}
        handleChatSubmit={handleSubmit}
      />
    </PortalLayout>
  );
};

const MyNavBarBtn = styled.button`
  display: flex;
  gap: 5px;
`;

const MyChatRoomItem = styled.section`
  display: flex;
  align-items: center;
  padding: 16px;
  gap: 8px;
  border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
`;

const MyChatRoomItemInfo = styled.div`
  display: flex;
  flex-direction: column;
  gap: 4px;
  & > span {
    text-align: left;
    ${({ theme }) => theme.fonts.subhead};
    color: ${({ theme }) => theme.colors.neutral.textStrong};
  }
  & > span:nth-child(2) {
    font-weight: 510;
  }
`;

const MyChatBubbles = styled.section`
  display: flex;
  flex-direction: column;
  padding: 16px;
  margin-bottom: 75px;
`;

const MyChatBubble = styled.div`
  width: fit-content;
  max-width: 65%;
  display: flex;
  padding: 6px 12px;
  margin-bottom: 16px;
  border-radius: 18px;
  & > span {
    text-align: left;
    ${({ theme }) => theme.fonts.body};
    color: ${({ theme }) => theme.colors.neutral.textStrong};
  }
`;

const MyBubble = styled(MyChatBubble)`
  background-color: ${({ theme }) => theme.colors.neutral.backgroundBold};
`;

const MyOpponentBubbleWrapper = styled.div`
  display: flex;
  justify-content: flex-end;
`;

const MyOpponentBubble = styled(MyChatBubble)`
  background-color: ${({ theme }) => theme.colors.accent.backgroundPrimary};
  & > span {
    color: ${({ theme }) => theme.colors.accent.text};
  }
`;

export default ChatRoom;
