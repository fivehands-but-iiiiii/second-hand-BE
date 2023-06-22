import Icon from '@assets/Icon';
import Item, { SaleItem } from '@common/Item';
import NavBar from '@common/NavBar';
import PopupSheet from '@common/PopupSheet';
import { REGION_MENU } from '@common/PopupSheet/constants';

import { styled } from 'styled-components';

interface ItemListProps {
  saleItems: SaleItem[];
  handleCategoryModal: () => void;
}

const ItemList = ({ saleItems, handleCategoryModal }: ItemListProps) => {
  return (
    <>
      <NavBar
        left={
          <MyNavBarBtn onClick={() => 'open region popup'}>
            역삼동
            <Icon name={'chevronDown'} />
          </MyNavBarBtn>
        }
        right={
          <button onClick={handleCategoryModal}>
            <Icon name={'hamburger'} />
          </button>
        }
      />
      <MyItemList>
        {saleItems.map((item) => (
          <Item key={item.id} item={item}></Item>
        ))}
      </MyItemList>
    </>
  );
};

const MyNavBarBtn = styled.button`
  display: flex;
  gap: 5px;

`;

const MyItemList = styled.div`
  /* TODO: 마지막 아이템 border: none 적용시키기 (아래 코드 작동 안 함) */
  &:last-child {
    border: none;
  }
`;
export default ItemList;
