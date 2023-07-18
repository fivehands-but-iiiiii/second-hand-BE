/* eslint-disable import/no-named-as-default-member */
import { useEffect, useState } from 'react';
import { createPortal } from 'react-dom';

import Button from '@common/Button';
import { SaleItem } from '@common/Item';
import NavBar from '@common/NavBar';
import Spinner from '@common/Spinner/Spinner';
import { CategoryInfo } from '@components/home/category';
import ItemList from '@components/home/ItemList/ItemList';
import useAPI from '@hooks/useAPI';
import useIntersectionObserver from '@hooks/useIntersectionObserver';

import { styled } from 'styled-components';

import { HomePageInfo } from '../pages/Home';

import ItemDetail from './ItemDetail';

// TODO: 상세 갔다가 뒤로 가도 리프레시, 카테고리 필터링해도 리프레시 적용, 아이템리스트 중복 제거
const WishList = () => {
  const title = '관심 목록';
  const [wishItems, setWishItems] = useState<SaleItem[]>([]);
  const [categoryInfo, setCategoryInfo] = useState<CategoryInfo[]>([]);
  const [categories, setCategories] = useState([{ id: 0, title: '전체' }]);
  const [selectedItem, setSelectedItem] = useState<number>(0);
  const [selectedCategoryId, setSelectedCategoryId] = useState(0);
  const [pageInfo, setPageInfo] = useState<HomePageInfo>({
    page: 0,
    hasPrevious: false,
    hasNext: true,
  });
  const [isLoading, setIsLoading] = useState(false);
  const { request } = useAPI();

  const onIntersect: IntersectionObserverCallback = ([{ isIntersecting }]) => {
    if (isIntersecting) getWishListItems();
  };

  const { setTarget } = useIntersectionObserver({ onIntersect });

  const getWishListItems = async () => {
    if (!pageInfo.hasNext) return;
    try {
      setIsLoading(true);
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
    } catch (error) {
      console.error(error);
    } finally {
      setIsLoading(false);
    }
  };

  const getWishListCategories = async () => {
    try {
      const { data } = await request({
        url: '/wishlist/categories',
        method: 'get',
      });
      const matchedCategories = data.categories.map((categoryId: number) => {
        const targetCategory = categoryInfo.find(({ id }) => id === categoryId);
        return (
          targetCategory && {
            id: targetCategory.id,
            title: targetCategory.title,
          }
        );
      });
      if (matchedCategories.length > 0) {
        setCategories((prevCategories) => {
          const newCategories = [...prevCategories, ...matchedCategories];
          const wishListCategories = newCategories.reduce(
            (acc: { id: number; title: string }[], category) => {
              if (category && !acc.some(({ id }) => id === category.id)) {
                acc.push(category);
              }
              return acc;
            },
            [],
          );
          return wishListCategories;
        });
      }
    } catch (error) {
      console.error(error);
    }
  };

  // TODO: 카테고리 fetch 전역에서 관리하기. 1번만 하도록 수정해야됨 (삭제 예정)
  const getCategoryInfo = async () => {
    if (categoryInfo.length) return;
    try {
      const { data } = await request({
        url: '/resources/categories',
        method: 'get',
      });
      setCategoryInfo(data.categories);
    } catch (error) {
      console.error(error);
    }
  };

  const handleFilterCategories = (categoryId: number) => {
    setSelectedCategoryId(categoryId);
    getFilteredItems();
  };

  const handleItemDetail = (itemId: number) => {
    setSelectedItem(itemId);
  };

  // TODO: 필터한다음 무한 스크롤 적용 테스트해야됨
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
  }, [wishItems]);

  useEffect(() => {
    getCategoryInfo();
    getWishListItems();
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
        {!!wishItems.length && (
          <MyOnFetchItems ref={setTarget}></MyOnFetchItems>
        )}
        {isLoading && <Spinner />}
        {!!selectedItem &&
          createPortal(
            <ItemDetail
              id={selectedItem}
              categoryInfo={categoryInfo}
              handleBackBtnClick={handleItemDetail}
            />,
            document.body,
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
