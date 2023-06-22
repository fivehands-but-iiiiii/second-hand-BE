import { useEffect, useState } from 'react';
import { createPortal } from 'react-dom';

import Icon from '@assets/Icon';
import { SaleItem } from '@common/Item';
import NavBar from '@common/NavBar/NavBar';
import Spinner from '@common/Spinner/Spinner';
import Category from '@components/home/Category';
import ItemList from '@components/home/ItemList';
import useIntersectionObserver from '@hooks/useIntersectionObserver';

import api from '../api';

interface HomeInfo {
  page: number;
  hasPrevious: boolean;
  hasNext: boolean;
  items: SaleItem[];
}

interface HomeFilterInfo {
  sellerId: number | null;
  regionId: number;
  isSales: boolean;
  categoryId: number | null;
}

type HomePageInfo = Omit<HomeInfo, 'items'>;

const Home = () => {
  const [saleItems, setSaleItems] = useState<SaleItem[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [filterInfo, setFilterInfo] = useState<HomeFilterInfo>({
    sellerId: null,
    regionId: 1,
    isSales: true,
    // TODO: 쿼리문에 아예 카테고리 키를 빼야만 전체 항목이 오는 상황, 조율 필요
    categoryId: null,
  });
  const [homePageInfo, setHomePageInfo] = useState<HomePageInfo>({
    page: 1,
    hasPrevious: false,
    hasNext: true,
  });

  const onIntersect: IntersectionObserverCallback = ([{ isIntersecting }]) => {
    if (isIntersecting) fetchItems();
  };

  const { setTarget } = useIntersectionObserver({ onIntersect });

  const createFilterQuery = () => {
    const { sellerId, regionId, isSales, categoryId } = filterInfo;
    const sellerQuery = sellerId ? `&sellerId=${sellerId}` : '';
    const categoryQuery = categoryId ? `&categoryId=${categoryId}` : '';
    const filterQuery = `?page=${homePageInfo.page}${sellerQuery}&regionId=${regionId}&isSales=${isSales}${categoryQuery}`;

    return filterQuery;
  };
  const [isCategoryModalOpen, setIsCategoryModalOpen] = useState(false);
  const handleCategoryModal = () => {
    setIsCategoryModalOpen((prev) => !prev);
  };
  const handleFilterCategory = (categoryId: number) => {
    // TODO: 카테고리로 필터 기능 setState 하기
    // setFilterInfo((prevFilterInfo) => ({
    //   ...prevFilterInfo,
    //   categoryId,
    // }));
  };

  const fetchItems = async () => {
    if (!homePageInfo.hasNext) return;

    const filterQuery = createFilterQuery();

    try {
      setIsLoading(true);

      const { data } = await api.get(`items/${filterQuery}`);

      setSaleItems((prevItems) => {
        const newSet = new Set(prevItems);
        data.items.forEach((item: SaleItem) => newSet.add(item));
        return [...newSet];
      });
      setHomePageInfo({
        page: data.number,
        hasPrevious: data.hasPrevious,
        hasNext: data.hasNext,
      });
    } catch (error) {
      console.error(`Failed to fetch items: ${error}`);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchItems();
  }, [filterInfo]);

  return (
    <>
      <ItemList
        saleItems={saleItems}
        handleCategoryModal={handleCategoryModal}
      />
      {!!saleItems.length && <div id="onFetchItems" ref={setTarget}></div>}
      {isLoading && <Spinner />}
      {isCategoryModalOpen &&
        createPortal(
          <Category
            handleCategoryModal={handleCategoryModal}
            onCategoryClick={handleFilterCategory}
          />,
          document.body,
        )}
    </>
  );
};
export default Home;
