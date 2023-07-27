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

interface ChatBubble {
  roomId: string;
  from: string;
  contents: string;
}

interface SaleItemSummary {
  id: number;
  title: 'string';
  price: number;
  thumbnailUrl: 'string';
  status: number;
  isDelete: boolean;
}

interface ChatRoomProps {
  itemId: number;
  onRoomClose: () => void;
}

const ChatRoom = ({ itemId, onRoomClose }: ChatRoomProps) => {
  const { id: userId } = getStoredValue({ key: 'userInfo' });

  const [itemInfo, setItemInfo] = useState<SaleItemSummary>(
    {} as SaleItemSummary,
  );
  const [roomId, setRoomId] = useState<Pick<ChatBubble, 'roomId'> | null>(null);
  const [opponentId, setOpponentId] = useState('');
  const [chatBubbles, setChatBubbles] = useState<ChatBubble[]>([]);
  const [chat, setChat] = useState('');
  const [isMoreViewPopupOpen, setIsMoreViewPopupOpen] = useState(false);

  const handleViewMorePopup = () => {
    setIsMoreViewPopupOpen(!isMoreViewPopupOpen);
  };

  const viewMorePopupSheetMenu = CHAT_VIEWMORE_MENU.map((menu) => ({
    ...menu,
    onClick: () => {
      console.log('');
      setIsMoreViewPopupOpen(false);
    },
  }));

  const endRef = useRef<HTMLDivElement | null>(null);

  const getChatInfo = async () => {
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
        // await getChatBubbles(chatroomId);
      }
    } catch (error) {
      // 오류 처리
    }
  };

  const createChatRoomId = async () => {
    try {
      const { data } = await api.post('/chats', {
        itemId,
      });

      setRoomId(data.data);
      return data.data;
    } catch (error) {
      // 오류 처리
    }
  };

  const getChatBubbles = async (chatroomId: number) => {
    const { data } = await api.get(`chat/${chatroomId}`);
    setChatBubbles(data);
  };

  useEffect(() => {
    getChatInfo();
  }, []);

  const client = useRef<StompJs.Client | null>(null);

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

  const publish = (chat: string) => {
    console.log('publish!!!!!!!!!!!!');
    if (!client.current?.connected) return;

    client.current.publish({
      destination: '/pub/message',
      body: JSON.stringify({
        roomId,
        from: userId,
        contents: chat,
      }),
    });

    setChat('');
  };

  const subscribe = () => {
    client.current?.subscribe(`/sub/${roomId}`, (body) => {
      const jsonBody = JSON.parse(body.body);
      console.log(jsonBody);
      setChatBubbles((_chatList) => [..._chatList, jsonBody]);
    });
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

  // 새로운 채팅방일 경우 로직
  // 1. createChatRoomId
  // 2. connect : 채팅 서버와 연결
  // 3. publish(roomId) : 2번이 되어야 3번이 가능

  useEffect(() => {
    // const isConnect = client.current?.connected;

    // if (roomId && !isConnect) {
    //   connect();
    // }

    roomId && connect();
    // connect();

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
      {!!chatBubbles.length && (
        <MyChatBubbles>
          {chatBubbles.map((bubble) => {
            const isMyBubble = bubble.from === '1';
            const BubbleComponent = isMyBubble ? MyBubble : MyPartnerBubble;

            const renderBubbleComponent = (
              <BubbleComponent>
                <span>{bubble.contents}</span>
              </BubbleComponent>
            );

            return isMyBubble ? (
              <>{renderBubbleComponent}</>
            ) : (
              <MyPartnerBubbleWrapper>
                {renderBubbleComponent}
              </MyPartnerBubbleWrapper>
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

const MyPartnerBubbleWrapper = styled.div`
  display: flex;
  justify-content: flex-end;
`;

const MyPartnerBubble = styled(MyChatBubble)`
  background-color: ${({ theme }) => theme.colors.accent.backgroundPrimary};
  & > span {
    color: ${({ theme }) => theme.colors.accent.text};
  }
`;

export default ChatRoom;
