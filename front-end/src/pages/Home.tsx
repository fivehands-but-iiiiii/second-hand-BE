import ItemList from '@components/home/ItemList/ItemList';

import { items } from '../mock/mockData';

const Home = () => {
  return (
    <>
      <ItemList saleItems={items} />
    </>
  );
};

export default Home;
