import { createContext, useEffect, useState, useContext } from 'react';

import { CategoryInfo } from '@components/home/category';
import useAPI from '@hooks/useAPI';

const CategoryContext = createContext<{ categories: CategoryInfo[] }>({
  categories: [],
});

interface CategoryProviderProps {
  children: JSX.Element | JSX.Element[];
}

const CategoryProvider = ({ children }: CategoryProviderProps): JSX.Element => {
  const [categories, setCategories] = useState<CategoryInfo[]>([]);
  const { request } = useAPI();

  const getCategories = async () => {
    if (categories.length) return;
    try {
      const { data } = await request({
        url: '/resources/categories',
        method: 'get',
      });
      setCategories(data.categories);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    getCategories();
  }, []);

  return (
    <CategoryContext.Provider value={{ categories }}>
      {children}
    </CategoryContext.Provider>
  );
};

export const useCategoryContext = () => useContext(CategoryContext);

export default CategoryProvider;
