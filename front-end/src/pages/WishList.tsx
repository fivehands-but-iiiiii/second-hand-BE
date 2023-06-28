/* eslint-disable import/no-named-as-default-member */
import { useEffect, useState } from 'react';

import Button from '@common/Button';
import { SaleItem } from '@common/Item';
import NavBar from '@common/NavBar';
import ItemList from '@components/home/ItemList/ItemList';
import useAPI from '@hooks/useAPI';
import api from 'api';
import axios from 'axios';

import { styled } from 'styled-components';

const WishList = () => {
  const title = '관심 목록';
  const [wishItems, setWishItems] = useState<SaleItem[]>([]);
  const [categories, setCategories] = useState([{ id: 0, title: '전체' }]);
  const [selectedCategoryId, setSelectedCategoryId] = useState(0);
  const { response, request } = useAPI();

  const getWishList = async () => {
    await axios
      .all([
        request({
          url: 'wishlist?page=1',
          method: 'get',
        }),
        request({
          url: '/wishlist/categories',
          method: 'get',
        }),
      ])
      .then(
        axios.spread((...responses) => {
          const [wishList, categories] = responses;
          setWishItems(wishList.data?.items);
          setCategories([categories, ...categories.data.categories]);
        }),
      );
  };

  const handleFilterCategories = (categoryId: number) => {
    setSelectedCategoryId(categoryId);
    getFilteredItems();
  };

  const getFilteredItems = async () => {
    try {
      const { data } = await api.get(
        `/wishlist?category=${selectedCategoryId}`,
      );
      setWishItems(data?.items);
    } catch (error) {
      console.error(`Failed to filter categories: ${error}`);
    }
  };

  useEffect(() => {
    getWishList();
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
        <ItemList saleItems={wishItems} />
      </MyWishList>
    </>
  );
};

const MyWishList = styled.div`
  padding: 0 2.7vw;
  height: calc(100%-83px);
`;

const MyCategories = styled.div`
  padding: 2vh 0;
  display: flex;
  gap: 4px;
`;

export default WishList;
