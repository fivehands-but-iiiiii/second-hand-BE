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
        <ul>
          {categories?.map(({ id, title }) => (
            <MyCategory
              active={id === selectedId}
              key={id}
              onClick={() => onClickCategory({ id, title })}
            >
              {title}
            </MyCategory>
          ))}
        </ul>
      </MyCategoryList>
    </PortalLayout>
  );
};

const MyCategoryList = styled.div`
  height: 89vh;
  padding: 0 3vw 10px;
  ul {
    height: 100%;
    ${({ theme }) => theme.fonts.subhead}
    color: ${({ theme }) => theme.colors.neutral.text};
    overflow: auto;
    > li:not(:last-child) {
      border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
    }
  }
`;

const MyCategory = styled.li<CategoryStyleProps>`
  color: ${({ theme, active }) =>
    active ? theme.colors.accent.backgroundPrimary : theme.colors.neutral.text};
  height: 6vh;
  line-height: 6vh;
  text-align: start;
`;

export default CategoryList;
