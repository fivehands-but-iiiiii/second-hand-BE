import Button from '@common/Button';
import ImgBox from '@common/ImgBox/ImgBox';

import { styled } from 'styled-components';

interface Category {
  id: number;
  title: string;
  imgUrl: string;
}

interface CategoryProps {
  category: Category[];
}

const Category = (category: CategoryProps) => {
  return (
    <MyCategoryContainer>
      {category.category.map((category) => (
        <MyCategory key={category.id} icon>
          <ImgBox src={category.imgUrl} size="sm" />
          {category.title}
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
