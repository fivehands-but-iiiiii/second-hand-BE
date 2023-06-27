import NavBar from '@common/NavBar';
import PortalLayout from '@components/layout/PortalLayout';

import { styled } from 'styled-components';

import { Category } from './itemEditor/ItemEditor';
interface CategoryListProps {
  categories: Category[];
  selectedId: number;
  onClickCategory: (category: Category) => void;
  onPortal: () => void;
}

interface CategoryStyleProps {
  active: boolean;
}

const CategoryList = ({
  categories,
  selectedId,
  onClickCategory,
  onPortal,
}: CategoryListProps) => {
  return (
    <PortalLayout>
      <NavBar
        left={<button onClick={onPortal}>닫기</button>}
        center={'카테고리'}
      ></NavBar>
      <MyCategoryList>
        {categories?.map(({ id, title }) => (
          <MyCategory
            active={id === selectedId}
            key={id}
            onClick={() => onClickCategory({ id, title })}
          >
            {title}
          </MyCategory>
        ))}
      </MyCategoryList>
    </PortalLayout>
  );
};

const MyCategoryList = styled.ul`
  position: absolute;
  width: 100vw;
  height: 89vh;
  ${({ theme }) => theme.fonts.subhead}
  color: ${({ theme }) => theme.colors.neutral.text};
  overflow-y: auto;
  -ms-overflow-style: none;
  &::-webkit-scrollbar {
    display: none;
  }
  > li {
    height: 6vh;
    line-height: 6vh;
    padding: 0 2.7vw;
    text-align: start;
    border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
  }
`;

const MyCategory = styled.li<CategoryStyleProps>`
  color: ${({ theme, active }) =>
    active ? theme.colors.accent.backgroundPrimary : theme.colors.neutral.text};
`;

export default CategoryList;
