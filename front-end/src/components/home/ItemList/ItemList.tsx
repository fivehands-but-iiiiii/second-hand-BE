import Item, { SaleItem } from '@common/Item';

import { styled } from 'styled-components';

interface ItemListProps {
  saleItems: SaleItem[];
  onItemClick: (id: number) => void;
}

const ItemList = ({ saleItems, onItemClick }: ItemListProps) => {
  return (
    <>
      <MyItemList>
        {saleItems.map((item) => (
          <Item key={item.id} item={item} onItemClick={onItemClick}></Item>
        ))}
      </MyItemList>
    </>
  );
};

const MyItemList = styled.div`
  > div:not(:last-child) {
    border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
  }
`;
export default ItemList;
