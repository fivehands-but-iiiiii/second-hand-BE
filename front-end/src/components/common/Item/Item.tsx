import { styled } from 'styled-components';

import ImgBox from '../ImgBox/ImgBox';

export interface saleItem {
  id: number;
  title: string;
  price: number;
  thumbnailUrl: string;
  status: 'onSale' | 'reservation' | 'soldOut';
  createdAt: string;
  hits: number;
  chatCount: number;
  likesCount: number;
  isLike: boolean;
}

interface ItemProps {
  item: saleItem;
}

const Item = ({ item }: ItemProps) => {
  const {
    id,
    title,
    price,
    thumbnailUrl,
    status,
    createdAt,
    chatCount,
    likesCount,
    isLike,
  } = item;

  return (
    <MyItem>
      <ImgBox src={thumbnailUrl} />
      <MyItemInfo>
        <MyItemTitle>
          <div>{title}</div>
          {/* TODO: add more info icon */}
          <div>...</div>
        </MyItemTitle>
        {/* TODO: add region */}
        <MyItemTime>{createdAt}</MyItemTime>
        <MyItemStatus>
          {/* TODO: add chip component */}
          <div>{status}</div>
          <div>{price}원</div>
        </MyItemStatus>
        <MyItemSubInfo>
          {/* TODO: add chat, likes icon with isLike prop*/}
          <div>{chatCount}</div>
          <div>{likesCount}</div>
        </MyItemSubInfo>
      </MyItemInfo>
    </MyItem>
  );
};

const MyItem = styled.div`
  display: flex;
  margin: 0 15px;
  padding: 15px 0;
  gap: 0 15px;
  color: ${({ theme }) => theme.colors.neutral.text};
  ${({ theme }) => theme.fonts.subhead}
  border-top: 1px solid ${({ theme }) => theme.colors.neutral.border};
`;

const MyItemInfo = styled.div`
  display: flex;
  width: 100%;
  flex-direction: column;
  padding: 4px 0;
  gap: 4px 0;
`;

const MyItemTitle = styled.div`
  display: flex;
  justify-content: space-between;
`;

const MyItemTime = styled.div`
  color: ${({ theme }) => theme.colors.neutral.textWeak};
  ${({ theme }) => theme.fonts.footnote}
`;

const MyItemStatus = styled.div`
  display: flex;
  gap: 0 4px;
  font-weight: 590;
  color: ${({ theme }) => theme.colors.neutral.textStrong};
`;

const MyItemSubInfo = styled.div`
  height: 100%;
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
  gap: 0 4px;
`;

export default Item;
