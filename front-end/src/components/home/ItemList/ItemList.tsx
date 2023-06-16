import Item, { SaleItem } from '@common/Item/Item';

import { styled } from 'styled-components';

interface SaleItems {
  saleItems: SaleItem[];
}

const ItemList = ({ saleItems }: SaleItems) => {
  return (
    <MyItemList>
      {saleItems.map((item) => (
        <Item item={item}></Item>
      ))}
    </MyItemList>
  );
};

const MyItemList = styled.div`
  &:last-child {
    border: none;
  }
`;
export default ItemList;
