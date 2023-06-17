import Item, { SaleItem } from '@common/Item';

import { styled } from 'styled-components';

interface SaleItems {
  saleItems: SaleItem[];
}

const ItemList = ({ saleItems }: SaleItems) => {
  return (
    <MyItemList>
      {saleItems.map((item) => (
        <Item key={item.id} item={item}></Item>
      ))}
    </MyItemList>
  );
};

const MyItemList = styled.div`
  /* TODO: 마지막 아이템 border: none 적용시키기 (아래 코드 작동 안 함) */
  &:last-child {
    border: none;
  }
`;
export default ItemList;
