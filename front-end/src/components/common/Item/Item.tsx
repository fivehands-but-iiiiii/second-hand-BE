import Icon from '@assets/Icon';
import Chip from '@common/Chip/Chip';

import { styled } from 'styled-components';

import { formatNumberToSI } from '../../../util/formatNumberToSI';
import ImgBox from '../ImgBox/ImgBox';

export interface SaleItem {
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
  item: SaleItem;
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
  const hasChip = status !== 'onSale';
  const formattedPrice = price.toLocaleString();

  return (
    <MyItem onClick={() => console.log(`move to item/${id}`)}>
      <ImgBox src={thumbnailUrl} />
      <MyItemInfo>
        <MyItemTitle>
          <div>{title}</div>
          {/* TODO: add more info icon */}
          <div>...</div>
        </MyItemTitle>
        <MyItemTime>
          {/* TODO: add region */}
          <div>역삼1동 &middot;</div>
          {createdAt}
        </MyItemTime>
        <MyItemStatus>
          {hasChip && <Chip status={status} />}
          <div>{formattedPrice}원</div>
        </MyItemStatus>
        <MyItemSubInfo>
          {!!chatCount && (
            <MyItemIcon>
              <Icon name="message" size="sm" />
              {formatNumberToSI(chatCount)}
            </MyItemIcon>
          )}
          {!!likesCount && (
            <MyItemIcon>
              {/* TODO: when icon's like value is false add black icon*/}
              {isLike ? '' : <Icon name="heart" size="sm" />}
              {formatNumberToSI(likesCount)}
            </MyItemIcon>
          )}
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
  cursor: pointer;
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
  display: flex;
  gap: 0 4px;
  color: ${({ theme }) => theme.colors.neutral.textWeak};
  ${({ theme }) => theme.fonts.footnote}
`;

const MyItemStatus = styled.div`
  display: flex;
  align-items: center;
  gap: 0 4px;
  font-weight: 590;
  color: ${({ theme }) => theme.colors.neutral.textStrong};
`;

const MyItemSubInfo = styled.div`
  height: 100%;
  display: flex;
  justify-content: flex-end;
  align-items: flex-end;
  gap: 0 5px;
`;

const MyItemIcon = styled.div`
  display: flex;
  align-items: flex-end;
  gap: 0 4px;
  ${({ theme }) => theme.fonts.footnote}
`;

export default Item;
