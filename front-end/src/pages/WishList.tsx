import { useEffect, useState } from 'react';
import { createPortal } from 'react-dom';

import Button from '@common/Button';
import { SaleItem } from '@common/Item';
import NavBar from '@common/NavBar';
import Spinner from '@common/Spinner/Spinner';
import { CategoryInfo } from '@components/home/category';
import ItemList from '@components/home/ItemList';
import { useCategories } from '@components/layout/MobileLayout';
import useAPI from '@hooks/useAPI';
import useIntersectionObserver from '@hooks/useIntersectionObserver';

import { styled } from 'styled-components';

import { HomePageInfo } from '../pages/Home';

import BlankPage from './BlankPage';
import ItemDetail from './ItemDetail';

type WishCategory = Omit<CategoryInfo, 'iconUrl'>;

const WishList = () => {
  const title = '관심 목록';
  const categories = useCategories();
  const [wishItems, setWishItems] = useState<SaleItem[]>([]);
  const [wishCategories, setWishCategories] = useState<WishCategory[]>([]);
  const [selectedItem, setSelectedItem] = useState(0);
  const [selectedCategoryId, setSelectedCategoryId] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const [onRefresh, setOnRefresh] = useState(false);
  const [pageInfo, setPageInfo] = useState<HomePageInfo>({
    page: 0,
    hasPrevious: false,
    hasNext: true,
  });
  const { request } = useAPI();

  const onIntersect: IntersectionObserverCallback = ([{ isIntersecting }]) => {
    if (isIntersecting && !isLoading) getWishListData();
  };

  const { setTarget } = useIntersectionObserver({ onIntersect });

  const getWishListData = async () => {
    if (!pageInfo.hasNext) return;
    try {
      setIsLoading(true);
      const [wishlistResponse, categoriesResponse] = await Promise.all([
        request({
          url: `wishlist?page=${pageInfo.page}${
            selectedCategoryId > 0 ? `&&category=${selectedCategoryId}` : ''
          }`,
        }),
        request({
          url: '/wishlist/categories',
        }),
      ]);
      const { data: itemData } = wishlistResponse;
      const { data: categoriesData } = categoriesResponse;
      matchCategories(categoriesData.categories);
      if(!itemData.items.length) {
        setSelectedCategoryId(0);
        initData();
        return;
      }
      setWishItems((pre) => [...pre, ...itemData.items]);
      setPageInfo({
        page: itemData.page + 1,
        hasPrevious: itemData.hasPrevious,
        hasNext: itemData.hasNext,
      });
    } catch (error) {
      console.error(error);
    } finally {
      setIsLoading(false);
    }
  };

  const matchCategories = (categoriesData: number[]) => {
    const matchedCategories = categoriesData.map((categoryId: number) => {
      const targetCategory = categories.find(({ id }) => id === categoryId);
      return (
        targetCategory && {
          id: targetCategory.id,
          title: targetCategory.title,
        }
      );
    });
    setWishCategories(
      [{ id: 0, title: '전체' }, ...matchedCategories.filter((item) => !!item?.id) as WishCategory[]])
  };

  const initData = () => {
    setPageInfo({
      page: 0,
      hasPrevious: false,
      hasNext: true,
    });
    setWishItems([]);
  };

  const handleFilterCategories = (categoryId: number) => {
    if (categoryId === selectedCategoryId) return;
    setSelectedCategoryId(categoryId);
    initData();
  };

  const handleItemDetail = (itemId: number) => {
    setSelectedItem(itemId);
    if(itemId) return;
    initData();
    setOnRefresh(true);
  };

  useEffect(() => {
    getWishListData();
  }, [selectedCategoryId]);

  useEffect(() => {
    if(!onRefresh) return;
    getWishListData();
    setOnRefresh(false);
  }, [onRefresh]);

  return (
    <>
      <NavBar center={title} />
      <MyWishList>
        {!!wishCategories.length && (
          <MyCategories>
            {wishCategories.map(({ id, title }) => {
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
        )}
        {!!wishItems.length ? (
          <>
            <ItemList saleItems={wishItems} onItemClick={handleItemDetail} />
            {!!wishItems.length && (
              <MyOnFetchItems ref={setTarget}></MyOnFetchItems>
            )}
            {isLoading && <Spinner />}
            {!!selectedItem &&
              createPortal(
                <ItemDetail
                  id={selectedItem}
                  categoryInfo={categories}
                  handleBackBtnClick={handleItemDetail}
                />,
                document.body,
              )}
          </>
        ) : (
          <BlankPage title={title} />
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
  overflow: auto;
  -ms-overflow-style: none;
  &::-webkit-scrollbar {
    display: none;
  }
  > button {
    min-width: max-content;
  }
`;

const MyOnFetchItems = styled.div`
  margin-bottom: 75px;
`;

export default WishList;
