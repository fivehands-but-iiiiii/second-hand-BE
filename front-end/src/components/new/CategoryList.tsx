import NavBar from '@common/NavBar/NavBar';
import PortalLayout from '@components/layout/PortalLayout';

import { styled } from 'styled-components';

import { Category } from './itemEditor/ItemEditor';
interface CategoryListProps {
  categories: Category[];
  selectedId: number;
  onClickCategory: (category: Category) => void;
  onPortal: () => void;
}

const CategoryList = ({
  categories,
  selectedId,
  onClickCategory,
  onPortal,
}: CategoryListProps) => {
  return (
    <PortalLayout>
      <>
        <NavBar
          type={'portal'}
          left={<button onClick={onPortal}>닫기</button>}
          center={'카테고리'}
        ></NavBar>
        <div>
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
        </div>
      </>
    </PortalLayout>
  );
};

const MyCategoryList = styled.ul`
  position: absolute;
  width: 100vw;
  overflow-y: auto;
  height: 85vh;
  ${({ theme }) => theme.fonts.subhead}
  color: ${({ theme }) => theme.colors.neutral.text};
  > li {
    height: 6vh;
    line-height: 6vh;
    padding: 0 2.7vw;
    text-align: start;
    border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
  }
`;

const MyCategory = styled.li<{ active: boolean }>`
  color: ${({ theme, active }) =>
    active ? theme.colors.accent.backgroundPrimary : theme.colors.neutral.text};
`;

export default CategoryList;
