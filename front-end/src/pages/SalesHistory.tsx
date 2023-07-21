import { useState, useEffect } from 'react';
import { createPortal } from 'react-dom';

import { SaleItem } from '@common/Item';
import NavBar from '@common/NavBar/NavBar';
import SegmentedControl from '@common/SegmentedControl';
import Spinner from '@common/Spinner/Spinner';
import { useCategoryContext } from '@components/context/CategoryContext';
import ItemList from '@components/home/ItemList/ItemList';
import { ItemStatus } from '@components/ItemStatus';
import useAPI from '@hooks/useAPI';
import useIntersectionObserver from '@hooks/useIntersectionObserver';
import ItemDetail from '@pages/ItemDetail';
import { getStoredValue } from '@utils/sessionStorage';

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

const SalesHistory = () => {
  const title = '판매 내역';
  const { categories } = useCategoryContext();
  // TODO: API 바뀌면 userInfo 필요없음
  const userInfo = getStoredValue({ key: 'userInfo' });
  const [saleItems, setSaleItems] = useState<SaleItem[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [selectedItem, setSelectedItem] = useState(0);
  const [selectedStatus, setSelectedStatus] = useState(ItemStatus.ON_SALE);
  const [onRefresh, setOnRefresh] = useState(false);
  const [pageInfo, setPageInfo] = useState<HomePageInfo>({
    page: 0,
    hasPrevious: false,
    hasNext: true,
  });
  const { request } = useAPI();

  const onIntersect: IntersectionObserverCallback = ([{ isIntersecting }]) => {
    if (isIntersecting && !isLoading) getSalesHistory();
  };

  const { setTarget } = useIntersectionObserver({ onIntersect });

  const getSalesHistory = async () => {
    if (!pageInfo.hasNext) return;
    try {
      setIsLoading(true);
      const data = await request({
        url: `items?page=${pageInfo.page}&sellerId=${
          userInfo.id
        }&isSales=${!selectedStatus}&categoryId=`,
        method: 'get',
      });
      setSaleItems((pre) => [...pre, ...data.items]);
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

  const initData = () => {
    setPageInfo({
      page: 0,
      hasPrevious: false,
      hasNext: true,
    });
    setSaleItems([]);
  };

  const handleSelectedStatus = (index: number) => {
    if (index === selectedStatus) return;
    setSelectedStatus(index);
    initData();
  };

  const handleItemDetail = (id: number) => {
    setSelectedItem(id);
    if (!id) {
      initData();
      setOnRefresh(true);
    }
  };

  useEffect(() => {
    getSalesHistory();
  }, [selectedStatus]);

  useEffect(() => {
    if (onRefresh) {
      getSalesHistory();
      setOnRefresh(false);
    }
  }, [onRefresh]);

  return (
    <>
      <NavBar center={title}>
        <SegmentedControl
          options={SALES_STATUS}
          value={selectedStatus}
          onClick={handleSelectedStatus}
        />
      </NavBar>
      {saleItems.length > 0 ? (
        <>
          <ItemList saleItems={saleItems} onItemClick={handleItemDetail} />
          {!!saleItems.length && (
            <MyOnFetchItems ref={setTarget}></MyOnFetchItems>
          )}
          {isLoading && <Spinner />}
        </>
      ) : (
        <BlankPage title={title} />
      )}
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
  );
};

const MyOnFetchItems = styled.div`
  margin-bottom: 75px;
`;

export default SalesHistory;
