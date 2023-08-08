import { useState, useEffect } from 'react';
import { createPortal } from 'react-dom';

import { SaleItem } from '@common/Item';
import NavBar from '@common/NavBar';
import SegmentedControl from '@common/SegmentedControl';
import Spinner from '@common/Spinner/Spinner';
import ItemList from '@components/home/ItemList';
import { ItemStatus } from '@components/ItemStatus';
import { useCategories } from '@components/layout/MobileLayout';
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
  const categories = useCategories();
  const [saleItems, setSaleItems] = useState<SaleItem[]>([]);
  const [selectedItem, setSelectedItem] = useState(0);
  const [selectedStatus, setSelectedStatus] = useState(ItemStatus.ON_SALE);
  const [onRefresh, setOnRefresh] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
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
    const userInfo = getStoredValue({ key: 'userInfo' });
    if (!userInfo) return;
    try {
      setIsLoading(true);
      const { data } = await request({
        url: `items/mine?page=${pageInfo.page}&isSales=${!selectedStatus}`,
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
      {!!saleItems.length ? (
        <>
          <ItemList
            saleItems={saleItems}
            onItemClick={handleItemDetail}
            // TODO : 더보기 버튼 추가 onHistoryPage={true}
          />
          {!!saleItems.length && (
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
    </>
  );
};

const MyOnFetchItems = styled.div`
  margin-bottom: 75px;
`;

export default SalesHistory;
