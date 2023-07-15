import { SetStateAction, useEffect, useRef, useState } from 'react';

import Icon from '@assets/Icon';
import ImgBox from '@common/ImgBox';
import { SaleItem } from '@common/Item';
import NavBar from '@common/NavBar';
import ChatTabBar from '@common/TabBar/ChatTabBar';
import PortalLayout from '@components/layout/PortalLayout';
import * as StompJs from '@stomp/stompjs';

import { styled } from 'styled-components';

import api from '../../../api';

interface ChatBubble {
  roomId: string;
  from: string;
  message: string;
}

interface ChatRoomProps {
  itemId: number;
}

type SaleItemSummary = Pick<
  SaleItem,
  'id' | 'title' | 'price' | 'thumbnailUrl' | 'status'
>;

const ChatRoom = ({ itemId }: ChatRoomProps) => {
  const [itemInfo, setItemInfo] = useState<SaleItemSummary>(
    // {} as SaleItemSummary,
    {
      id: 230,
      title: '팝니다요',
      price: 10000,
      thumbnailUrl: 'https://picsum.photos/200/300',
      status: 0,
    },
  );
  const [roomId, setRoomId] = useState<Pick<ChatBubble, 'roomId'> | null>(null);
  const [opponentId, setOpponentId] = useState('');
  const [chatBubbles, setChatBubbles] = useState<ChatBubble[]>([]);
  const [chat, setChat] = useState('');

  const endRef = useRef<HTMLDivElement | null>(null);

  const getChatInfo = async () => {
    try {
      const { data } = await api.get(`chats/items/${itemId}`);

      setItemInfo(data.item);
      setRoomId(data.chatId);
      setOpponentId(data.opponentId);

      if (roomId) {
        await getChatBubbles();
      } else {
        // TODO: chatId, chatBubbles가 null이고 first bubble이 입력되면 chatId를 생성
        await createChatId();
      }
    } catch (error) {
      // 오류 처리
    }
  };

  const createChatId = async () => {
    try {
      const { data } = await api.post('chats', {
        itemId,
      });
      setRoomId(data.chatId);
    } catch (error) {
      // 오류 처리
    }
  };

  const getChatBubbles = async () => {
    const { data } = await api.get(`chat/${roomId}`);
    setChatBubbles(data);
  };

  // useEffect(() => {
  //   getChatInfo();
  // }, []);

  const client = useRef<StompJs.Client | null>(null);

  const connect = () => {
    client.current = new StompJs.Client({
      brokerURL: 'ws://3.37.51.148:81/chat',
      onConnect: () => {
        subscribe();
      },
    });
    client.current.activate();
  };

  const publish = (chat: string) => {
    if (!client.current?.connected) return;

    client.current.publish({
      destination: '/pub/message',
      body: JSON.stringify({
        roomId: 'room1',
        from: 2,
        message: chat,
      }),
    });

    setChat('');
  };

  const subscribe = () => {
    client.current?.subscribe('/sub/room1', (body) => {
      const jsonBody = JSON.parse(body.body);
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

  const handleSubmit = (chat: string) => {
    publish(chat);
  };

  useEffect(() => {
    // TODO: roomId && connect();
    connect();

    return () => disconnect();
  }, [roomId]);

  useEffect(() => {
    endRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [chatBubbles]);

  return (
    <PortalLayout>
      <NavBar
        left={
          <MyNavBarBtn onClick={() => console.log('뒤로')}>
            <Icon name={'chevronLeft'} />
            <span>뒤로</span>
          </MyNavBarBtn>
        }
        center={opponentId}
        right={
          <button onClick={() => console.log('handleViewMorePopup')}>
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
                <span>{bubble.message}</span>
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
  overflow: scroll;
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
