import { ChangeEvent, useState } from 'react';
import { createPortal } from 'react-dom';

import Icon from '@assets/Icon';
import Button from '@common/Button';
import Textarea from '@common/Textarea';

import { styled } from 'styled-components';

import CategoryList from '../CategoryList';
import { CategoryInfo, Category } from '../itemEditor/ItemEditor';

interface TitleEditorProps {
  title: string;
  category: CategoryInfo;
  onChageTitle: (e: ChangeEvent<HTMLTextAreaElement>) => void;
  onClickTitle: () => void;
  onClickCategory: (category: Category) => void;
}

const TitleEditor = ({
  title,
  category,
  onChageTitle,
  onClickTitle,
  onClickCategory,
}: TitleEditorProps) => {
  const [isCategoryModalOpen, setIsCategoryModalOpen] = useState(false);
  const handleCategoryModal = () => {
    setIsCategoryModalOpen((prev) => !prev);
  };

  return (
    <MyTitleBox>
      <Textarea
        name={'title'}
        value={title}
        placeholder="글 제목"
        rows={title.length > 30 ? 2 : 1}
        maxLength={64}
        onChange={onChageTitle}
        onClick={onClickTitle}
      />
      {!!category.recommendedCategory.length && (
        <MyTitleCategories>
          <MyCategories>
            {category.recommendedCategory.map(({ id, title }: Category) => {
              const isActive = category.currentId === id;
              return (
                <Button
                  key={id}
                  active={isActive}
                  category
                  onClick={() => onClickCategory({ id, title })}
                >
                  {title}
                </Button>
              );
            })}
          </MyCategories>
          <Icon
            name={'chevronRight'}
            size={'xs'}
            onClick={handleCategoryModal}
          />
        </MyTitleCategories>
      )}
      {isCategoryModalOpen &&
        createPortal(
          <CategoryList
            categories={category.total}
            selectedId={category.currentId}
            onClickCategory={onClickCategory}
            onPortal={handleCategoryModal}
          />,
          document.body,
        )}
    </MyTitleBox>
  );
};

const MyTitleBox = styled.div`
  border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
`;

const MyTitleCategories = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding-bottom: 15px;
`;

const MyCategories = styled.div`
  display: flex;
  gap: 4px;
`;

export default TitleEditor;
