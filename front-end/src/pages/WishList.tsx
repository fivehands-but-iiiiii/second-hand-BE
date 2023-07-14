import { useEffect, useState } from 'react';
import { createPortal } from 'react-dom';

import Button from '@common/Button';
import { SaleItem } from '@common/Item';
import NavBar from '@common/NavBar';
import { CategoryInfo } from '@components/home/category';
import ItemList from '@components/home/ItemList/ItemList';
import useAPI from '@hooks/useAPI';
import useIntersectionObserver from '@hooks/useIntersectionObserver';

import { styled } from 'styled-components';

import { HomePageInfo } from '../pages/Home';

import ItemDetail from './ItemDetail';

const WishList = () => {
  const title = '관심 목록';
  const [wishItems, setWishItems] = useState<SaleItem[]>([]);
  const [categories, setCategories] = useState<CategoryInfo[]>([
    { id: 0, title: '전체', iconUrl: '' },
  ]);
  const [selectedCategoryId, setSelectedCategoryId] = useState(0);
  const [selectedItem, setSelectedItem] = useState(0);
  const [pageInfo, setPageInfo] = useState<HomePageInfo>({
    page: 0,
    hasPrevious: false,
    hasNext: true,
  });
  const { request } = useAPI();

  const onIntersect: IntersectionObserverCallback = ([{ isIntersecting }]) => {
    if (isIntersecting) getWishListItems();
  };

  const { setTarget } = useIntersectionObserver({ onIntersect });

  const getWishListItems = async () => {
    if (!pageInfo.hasNext) return;
    const { data } = await request({
      url: `wishlist?page=${pageInfo.page}`,
      method: 'get',
    });
    setWishItems((pre) => [...pre, ...data.items]);
    setPageInfo({
      page: data.page + 1,
      hasPrevious: data?.hasPrevious,
      hasNext: data?.hasNext,
    });
  };

  const getWishListCategories = async () => {
    const { data } = await request({
      url: '/wishlist/categories',
      method: 'get',
    });
    setCategories((pre) => [...pre, ...data.categories]);
  };

  const handleFilterCategories = (categoryId: number) => {
    setSelectedCategoryId(categoryId);
    getFilteredItems();
  };

  const handleItemDetail = (itemId: number) => {
    setSelectedItem(itemId);
  };

  const getFilteredItems = async () => {
    const { data } = await request({
      url: `wishlist?category=${selectedCategoryId}`,
      method: 'get',
    });
    setWishItems(data.items);
  };

  useEffect(() => {
    getWishListItems();
    getWishListCategories();
  }, []);

  return (
    <>
      <NavBar center={title} />
      <MyWishList>
        <MyCategories>
          {categories.map(({ id, title }) => {
            const isActive = id === selectedCategoryId;
            return (
              <Button
                key={id}
                active={isActive}
                category
                onClick={() => handleFilterCategories(id)}
              >
                {title}
              </Button>
            );
          })}
        </MyCategories>
        <ItemList saleItems={wishItems} onItemClick={handleItemDetail} />
        {!!selectedItem &&
          createPortal(
            <ItemDetail
              id={selectedItem}
              categoryInfo={categories}
              handleBackBtnClick={handleItemDetail}
            />,
            document.body,
          )}
        {!!wishItems.length && (
          <MyOnFetchItems ref={setTarget}></MyOnFetchItems>
        )}
      </MyWishList>
    </>
  );
};

const MyWishList = styled.div`
  height: calc(90vh-83px);
  overflow: auto;
  -ms-overflow-style: none;
  &::-webkit-scrollbar {
    display: none;
  }
`;

const MyCategories = styled.div`
  padding: 2vh 15px 0;
  display: flex;
  gap: 4px;
`;

const MyOnFetchItems = styled.div`
  margin-bottom: 75px;
`;

export default WishList;
