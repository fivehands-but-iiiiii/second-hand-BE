import { MouseEvent } from 'react';

import Icon from '@assets/Icon';
import Chip from '@common/Chip';
import { ItemStatus } from '@components/ItemStatus';
import { formatNumberToSI } from '@utils/formatNumberToSI';
import { getFormattedPrice } from '@utils/formatPrice';
import getElapsedTime from '@utils/getElapsedTime';

import { styled } from 'styled-components';

import ImgBox from '../ImgBox';

export interface Region {
  id: number;
  city: string;
  county: string;
  district: string;
}

export interface SaleItem {
  id: number;
  title: string;
  price: number;
  thumbnailUrl: string;
  status: ItemStatus;
  createdAt: string;
  region: Region;
  hits: number;
  chatCount: number;
  likeCount: number;
  isLike: boolean;
}

interface ItemProps {
  item: SaleItem;
  onHistoryPage?: boolean;
  onItemClick: (id: number) => void;
  onViewMoreButton?: (id: number) => void;
}

const Item = ({
  item,
  onHistoryPage = false,
  onItemClick,
  onViewMoreButton,
}: ItemProps) => {
  const {
    id,
    title,
    price,
    region,
    thumbnailUrl,
    status,
    createdAt,
    chatCount,
    likeCount,
    isLike,
  } = item;
  const hasChip = status !== ItemStatus.ON_SALE;

  const handleItemClick = () => onItemClick(id);

  const handleViewMoreButton = (id: number, event: MouseEvent) => {
    event.stopPropagation();
    onViewMoreButton && onViewMoreButton(id);
  };

  return (
    <MyItem onClick={handleItemClick}>
      <ImgBox src={thumbnailUrl} alt={title} />
      <MyItemInfo>
        <MyItemTitle>
          <div>{title}</div>
          {onHistoryPage && (
            <Icon
              name="ellipsis"
              size="sm"
              onClick={(event) => handleViewMoreButton(id, event)}
            />
          )}
        </MyItemTitle>
        <MyItemTime>
          <div>{region.district}</div>
          {getElapsedTime(createdAt)}
        </MyItemTime>
        <MyItemStatus>
          {hasChip && <Chip status={status} />}
          <div>{getFormattedPrice(price)}</div>
        </MyItemStatus>
        <MyItemSubInfo>
          {!!chatCount && (
            <MyItemIcon>
              <Icon name="message" size="sm" />
              {formatNumberToSI(chatCount)}
            </MyItemIcon>
          )}
          {!!likeCount && (
            // TODO: MyItemIcon component 분리 (여러 곳에서 쓰임)
            <MyItemIcon>
              {<Icon name={isLike ? 'fullHeart' : 'heart'} size="sm" />}
              {formatNumberToSI(likeCount)}
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
