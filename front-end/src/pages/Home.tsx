import { useState } from 'react';

import { SaleItem } from '@common/Item';
import ItemList from '@components/home/ItemList';
import useIntersectionObserver from '@hooks/useIntersectionObserver';
import axios from 'axios';

const Home = () => {
  const [saleItems, setSaleItems] = useState<SaleItem[]>([]);
  const [maxPage, setMaxPage] = useState<number>(1);
  const [page, setPage] = useState<number>(1);

  const onIntersect: IntersectionObserverCallback = ([{ isIntersecting }]) => {
    if (isIntersecting) fetchItems();
  };

  const { setTarget } = useIntersectionObserver({ onIntersect });

  const fetchItems = async () => {
    try {
      if (page > maxPage) return;

      const { data } = await axios.get(
        `http://13.125.243.239/items?page=${page}&region=1`,
      );

      setSaleItems((prevItems) => [...prevItems, ...data.items]);
      setMaxPage(data.maxPage);
      setPage((prevPage) => prevPage + 1);
    } catch (error) {
      console.error(`Failed to fetch items: ${error}`);
    }
  };

  return (
    <>
      <ItemList saleItems={saleItems} />
      <div id="onFetchItems" ref={setTarget}></div>
    </>
  );
};

export default Home;
