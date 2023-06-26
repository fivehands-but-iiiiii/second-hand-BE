import { ChangeEvent } from 'react';

import Icon from '@assets/Icon';
import Button from '@common/Button';
import Textarea from '@common/Textarea';

import { styled } from 'styled-components';

import { CategoryInfo, Category } from '../NewItemEditor';

interface TitleEditorProps {
  title: string;
  categoryInfo: CategoryInfo;
  onChageTitle: (e: ChangeEvent<HTMLTextAreaElement>) => void;
  onClickTitle: () => void;
  onClickCategory: (categoryId: number) => void;
}

const TitleEditor = ({
  title,
  categoryInfo,
  onChageTitle,
  onClickTitle,
  onClickCategory,
}: TitleEditorProps) => {
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
      {categoryInfo.recommendedCategory.length === 3 && (
        <MyTitleCategories>
          <MyCategories>
            {categoryInfo.recommendedCategory.map(({ id, title }: Category) => {
              const isActive = categoryInfo.currentId === id;
              return (
                <Button
                  key={id}
                  active={isActive}
                  category
                  onClick={() => onClickCategory(id)}
                >
                  {title}
                </Button>
              );
            })}
          </MyCategories>
          <Icon
            name={'chevronRight'}
            size={'xs'}
            onClick={() => {
              console.log('카테고리 포탈띄우기');
            }}
          />
        </MyTitleCategories>
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
