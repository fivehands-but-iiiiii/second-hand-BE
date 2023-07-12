import { useEffect, useState } from 'react';

import Icon from '@assets/Icon';
import ImgBox from '@common/ImgBox';
import { SaleItem } from '@common/Item';
import NavBar from '@common/NavBar';

import { styled } from 'styled-components';

import api from '../../../api';

interface ChatBubbles {
  senderId: number;
  contents: string;
}

interface ChatRoomProps {
  itemId: number;
}

type SaleItemSummary = Pick<
  SaleItem,
  'id' | 'title' | 'price' | 'thumbnailUrl' | 'status'
>;

const ChatRoom = ({ itemId }: ChatRoomProps) => {
  const [opponentId, setOpponentId] = useState('');
  const [itemInfo, setItemInfo] = useState<SaleItemSummary>(
    {} as SaleItemSummary,
  );
  const [chatId, setChatId] = useState<number | null>(null);
  const [chatBubbles, setChatBubbles] = useState<ChatBubbles[]>([]);

  const getChatInfo = async () => {
    try {
      const { data } = await api.get(`chats/items/${itemId}`);

      setOpponentId(data.opponentId);
      setItemInfo(data.item);
      setChatId(data.chatId);

      if (chatId) {
        await getChatBubbles();
      } else {
        await createChatId();
      }
    } catch (error) {
      // 오류 처리
    }
  };

  const createChatId = async () => {
    // TODO: chatId, chatBubbles가 null이고 first bubble이 입력되면 chatId를 생성
    try {
      const { data } = await api.post('chats', {
        itemId,
      });
      setChatId(data.chatId);
    } catch (error) {
      // 오류 처리
    }
  };

  const getChatBubbles = async () => {
    const { data } = await api.get(`chat/${chatId}`);
    setChatBubbles(data);
  };

  useEffect(() => {
    getChatInfo();
  }, []);

  return (
    <>
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
      <MyChatBubbles>
        {chatBubbles.map((bubble) => {
          const BubbleComponent =
            bubble.senderId === 1 ? MyBubble : MyPartnerBubble;
          return (
            // key 추가하기
            <BubbleComponent>
              <span>{bubble.contents}</span>
            </BubbleComponent>
          );
        })}
      </MyChatBubbles>
    </>
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
`;

const MyChatBubble = styled.div`
  width: fit-content;
  max-width: 65%;
  display: flex;
  padding: 6px 12px;
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
