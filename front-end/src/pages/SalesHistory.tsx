import { useState, useEffect } from 'react';
import { createPortal } from 'react-dom';

import { SaleItem } from '@common/Item';
import NavBar from '@common/NavBar/NavBar';
import SegmentedControl from '@common/SegmentedControl';
import Spinner from '@common/Spinner/Spinner';
import { CategoryInfo } from '@components/home/category';
import ItemList from '@components/home/ItemList/ItemList';
import { ItemStatus } from '@components/ItemStatus';
import useAPI from '@hooks/useAPI';
import useIntersectionObserver from '@hooks/useIntersectionObserver';
import ItemDetail from '@pages/ItemDetail';

import { styled } from 'styled-components';

import BlankPage from './BlankPage';
import { HomePageInfo } from './Home';

const SALES_STATUS = [
  {
    status: ItemStatus.ON_SALE,
    label: '판매중',
  },
  {
    status: ItemStatus.SOLD_OUT,
    label: '판매완료',
  },
];

// TODO: 스크롤이 너무나 무한으로 돌아가는 현상 해결해야됨
const SalesHistory = () => {
  const title = '판매 내역';
  const [categoryInfo, setCategoryInfo] = useState<CategoryInfo[]>([]);
  const [selectedIndex, setSelectedIndex] = useState(ItemStatus.ON_SALE);
  const [saleItems, setSaleItems] = useState<SaleItem[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [selectedItem, setSelectedItem] = useState<number>(0);
  const [pageInfo, setPageInfo] = useState<HomePageInfo>({
    page: 0,
    hasPrevious: false,
    hasNext: true,
  });
  const { request } = useAPI();

  const onIntersect: IntersectionObserverCallback = ([{ isIntersecting }]) => {
    if (isIntersecting) getSalesHistory();
  };

  const { setTarget } = useIntersectionObserver({ onIntersect });

  const getSalesHistory = async () => {
    if (!pageInfo.hasNext) return;
    try {
      setIsLoading(true);
      const data = await request({
        url: `items?page=${
          pageInfo.page
        }&regionId=1&isSales=${!selectedIndex}&categoryId=`,
        method: 'get',
      });
      setSaleItems(data.items);
      setPageInfo({
        page: data.number + 1,
        hasPrevious: data.hasPrevious,
        hasNext: data.hasNext,
      });
    } catch (error) {
      console.error(error);
    } finally {
      setIsLoading(false);
    }
  };

  const getCategoryInfo = async () => {
    if (categoryInfo.length) return;
    try {
      const { data } = await request({
        url: '/resources/categories',
        method: 'get',
      });
      setCategoryInfo(data.categories);
    } catch (error) {
      console.error(`Failed to get category icons: ${error}`);
    }
  };

  const handleSelectedIndex = (index: number) => {
    setSelectedIndex(index);
    setPageInfo({
      page: 0,
      hasPrevious: false,
      hasNext: true,
    });
  };

  const handleItemDetail = (id: number) => {
    setSelectedItem(id);
  };

  useEffect(() => {
    getSalesHistory();
  }, [!selectedIndex]);

  useEffect(() => {
    getSalesHistory();
    getCategoryInfo();
  }, []);

  return (
    <>
      <NavBar center={title}>
        <SegmentedControl
          options={SALES_STATUS}
          value={selectedIndex}
          onClick={handleSelectedIndex}
        />
      </NavBar>
      {saleItems.length > 0 ? (
        <ItemList saleItems={saleItems} onItemClick={handleItemDetail} />
      ) : (
        <BlankPage title={title} />
      )}
      {!!saleItems.length && <MyOnFetchItems ref={setTarget}></MyOnFetchItems>}
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
    </>
  );
};

const MyOnFetchItems = styled.div`
  margin-bottom: 75px;
`;

export default SalesHistory;
