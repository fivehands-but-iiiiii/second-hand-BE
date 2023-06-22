import { useEffect, useState } from 'react';

import Button from '@common/Button';
import ImgBox from '@common/ImgBox';

import { styled } from 'styled-components';

import api from '../../../api';


interface Category {
  id: number;
  title: string;
  iconUrl: string;
}

const Category = () => {
  const [categoryIcons, setCategoryIcons] = useState<Category[]>([]);

  const getCategoryIcons = async () => {
    try {
      const { data: { data } } = await api.get('/resources/categories');

      setCategoryIcons(data.categories);
    } catch (error) {
      console.error(`Failed to get category icons: ${error}`);
    }
  };

  useEffect(() => {
    getCategoryIcons()
  },[]);

  
  return (
    <MyCategoryContainer>
      {categoryIcons?.map((icon) => (
        <MyCategory key={icon.id} icon>
          <ImgBox src={icon.iconUrl} size="sm" alt={icon.title} />
          {icon.title}
        </MyCategory>
      ))}
    </MyCategoryContainer>
  );
};

const MyCategoryContainer = styled.div`
  width: 100%;
  margin-top: 40px;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(6, 1fr);
  gap: 20px 0;
`;

const MyCategory = styled(Button)`
  ${({ theme }) => theme.fonts.footnote};
`;

export default Category;
