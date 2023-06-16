import Item, { SaleItem } from '@common/Item/Item';

interface SaleItems {
  saleItems: SaleItem[];
}

const ItemList = ({ saleItems }: SaleItems) => {
  return (
    <>
      {saleItems.map((item) => (
        <Item item={item}></Item>
      ))}
    </>
  );
};

export default ItemList;
