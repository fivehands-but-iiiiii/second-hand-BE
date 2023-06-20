import { useState } from 'react';

import { SaleItem } from '@common/Item';
import Spinner from '@common/Spinner/Spinner';
import ItemList from '@components/home/ItemList';
import useIntersectionObserver from '@hooks/useIntersectionObserver';

import api from '../api';

const Home = () => {
  const [saleItems, setSaleItems] = useState<SaleItem[]>([]);
  const [maxPage, setMaxPage] = useState(1);
  const [page, setPage] = useState(1);
  const [isLoading, setIsLoading] = useState(false);

  const onIntersect: IntersectionObserverCallback = ([{ isIntersecting }]) => {
    if (isIntersecting) fetchItems();
  };

  const { setTarget } = useIntersectionObserver({ onIntersect });

  const fetchItems = async () => {
    try {
      if (page > maxPage) return;
      setIsLoading(true);

      const { data } = await api.get(`/items?page=${page}&region=1`);

      setSaleItems((prevItems) => [...prevItems, ...data.items]);
      setMaxPage(data.maxPage);
      setPage((prevPage) => prevPage + 1);
    } catch (error) {
      console.error(`Failed to fetch items: ${error}`);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <ItemList saleItems={saleItems} />
      {saleItems && <div id="onFetchItems" ref={setTarget}></div>}
      {isLoading && <Spinner />}
    </>
  );
};

export default Home;
