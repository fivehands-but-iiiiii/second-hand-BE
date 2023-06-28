import Icon from '@assets/Icon';
import Button from '@common/Button';
import ImgBox from '@common/ImgBox';
import NavBar from '@common/NavBar/NavBar';

import { styled } from 'styled-components';

export interface CategoryInfo {
  id: number;
  title: string;
  iconUrl: string;
}

interface CategoryProps {
  categoryInfo: CategoryInfo[];
  handleCategoryModal: () => void;
  onCategoryClick?: (categoryId: number) => void;
}

const Category = ({
  categoryInfo,
  handleCategoryModal,
  onCategoryClick,
}: CategoryProps) => {
  const handleCategoryClick = (categoryId: number) => {
    handleCategoryModal();
    onCategoryClick && onCategoryClick(categoryId);
  };

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
        {categoryInfo?.map((icon) => (
          <Button
            key={icon.id}
            icon
            onClick={() => handleCategoryClick(icon.id)}
          >
            <ImgBox
              src={icon.iconUrl}
              size="sm"
              alt={icon.title}
              loading="lazy"
            />
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
  width: 100%;
  height: 100%;
  background-color: ${({ theme }) => theme.colors.neutral.background};
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
