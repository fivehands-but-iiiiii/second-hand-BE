import { useEffect, useState } from 'react';

import Icon from '@assets/Icon';
import Button from '@common/Button';
import ImgBox from '@common/ImgBox';
import NavBar from '@common/NavBar/NavBar';

import { styled } from 'styled-components';

import api from '../../../api';

interface Category {
  id: number;
  title: string;
  iconUrl: string;
}

interface CategoryProps {
  handleCategoryModal: () => void;
  onCategoryClick?: (categoryId: number) => void;
}

const Category = ({ handleCategoryModal, onCategoryClick }: CategoryProps) => {
  const [categoryIcons, setCategoryIcons] = useState<Category[]>([]);
  // TODO: Main Tabbar 안 보이게 하기

  const handleCategoryClick = (categoryId: number) => {
    onCategoryClick && onCategoryClick(categoryId);
  };

  const getCategoryIcons = async () => {
    try {
      const {
        data: { data },
      } = await api.get('/resources/categories');

      setCategoryIcons(data.categories);
    } catch (error) {
      console.error(`Failed to get category icons: ${error}`);
    }
  };

  useEffect(() => {
    getCategoryIcons();
  }, []);

  return (
    <MyCategoryModal>
      <NavBar
        left={
          <MyCategoryCloseBtn onClick={handleCategoryModal}>
            <Icon name={'chevronLeft'} />
            닫기
          </MyCategoryCloseBtn>
        }
        center={'카테고리'}
      />
      <MyCategoryContainer>
        {categoryIcons?.map((icon) => (
          <Button
            key={icon.id}
            icon
            onClick={() => handleCategoryClick(icon.id)}
          >
            <ImgBox src={icon.iconUrl} size="sm" alt={icon.title} />
            <MyCategoryTitle>{icon.title}</MyCategoryTitle>
          </Button>
        ))}
      </MyCategoryContainer>
    </MyCategoryModal>
  );
};

const MyCategoryModal = styled.div`
  position: absolute;
  top: 0;
`;

const MyCategoryCloseBtn = styled.button`
  display: flex;
  gap: 5px;
`;

const MyCategoryContainer = styled.div`
  width: 100%;
  margin-top: 40px;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(6, 1fr);
  gap: 20px 0;
`;

const MyCategoryTitle = styled.span`
  ${({ theme }) => theme.fonts.footnote};
`;

export default Category;
