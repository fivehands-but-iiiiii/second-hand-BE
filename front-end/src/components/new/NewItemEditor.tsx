import {
  useEffect,
  useState,
  ChangeEvent,
  MouseEvent,
  useCallback,
} from 'react';

import Icon from '@assets/Icon';
import Button from '@common/Button/Button';
import Fileinput from '@common/FileInput';
import ImgBox from '@common/ImgBox/ImgBox';
import LabelInput from '@common/LabelInput';
import NavBar from '@common/NavBar';
import SubTabBar from '@common/TabBar/SubTabBar';
import Textarea from '@common/Textarea/Textarea';
import { InputFile } from '@components/login/Join';
import palette from '@styles/colors';
import { getPreviewURL } from '@utils/convertFile';
import { getFormattedPrice } from '@utils/formatText';

import { styled, keyframes } from 'styled-components';

import api from '../../api';

interface Category {
  id: number;
  title: string;
}

interface ImageFile {
  order: number;
  url: string;
}

export interface ItemInfo {
  id?: number;
  title: string;
  contents: string;
  category: number;
  price: string;
  images: ImageFile[];
}

interface NewItemEditorProps {
  isEdit?: boolean;
  origin?: ItemInfo;
  handleClose: () => void;
}

const NewItemEditor = ({
  isEdit = false,
  origin,
  handleClose,
}: NewItemEditorProps) => {
  const userRegion = '현재 지역'; // TODO: 유저 정보에서 region 받아오기
  const [firstClickCategory, setFirstClickCategory] = useState(false);
  const [currentCategoryId, setCurrentCategoryId] = useState(0);
  const [recommendedCategory, setRecommendedCategory] = useState<Category[]>(
    [],
  );
  const [files, setFiles] = useState<InputFile[]>([]);
  const [item, setItem] = useState<ItemInfo>({
    title: '',
    contents: '',
    category: 0,
    price: '',
    images: [],
  });

  const handleFiles = async ({ target }: ChangeEvent<HTMLInputElement>) => {
    const file = target.files?.[0];
    if (!file) return;
    if (files.length >= 10) return;
    const newPreviewURL = await getPreviewURL(file);
    setFiles((prev) => [
      ...prev,
      {
        preview: newPreviewURL,
        file: file,
      },
    ]);
  };

  // TODO: api 변경에 따라 로직 수정 필요함
  const handleSubmit = async () => {
    // const formData = new FormData();
    // files.forEach(({ file }) => {
    //   formData.append('images', file as Blob, file?.name);
    // });
    // formData.append('title', item.title);
    // formData.append('contents', item.contents);
    // formData.append('category', item.category.toString());
    // formData.append('price', item.price.toString());
    // // formData.append('region', item.region.toString());
    // // formData.append('firstImageUrl', item.firstImageUrl.url);
    // if (isEdit && origin) {
    //   await api.put(`/items/${origin.id}`, formData);
    // } else {
    //   await api.post('/items', formData);
    // }
    // handleClose();
    console.log(item);
  };

  const handleDeleteFile = ({
    currentTarget,
  }: MouseEvent<HTMLButtonElement>) => {
    const { value } = currentTarget;
    setFiles((prev) => prev.filter((_, index) => index !== Number(value)));
  };

  const handleTitle = ({ target }: ChangeEvent<HTMLTextAreaElement>) => {
    const { value } = target;
    setItem((prev) => ({ ...prev, title: value }));
  };

  const getRandomCategories = useCallback((): Category[] => {
    const RANDOM_COUNT = 3;
    const randomCategories: Set<Category> = new Set();
    while (randomCategories.size < RANDOM_COUNT) {
      const randomIndex = Math.floor(
        Math.random() * recommendedCategory.length,
      );
      randomCategories.add(recommendedCategory[randomIndex]);
    }
    const titleCategories = [...randomCategories];
    return titleCategories;
  }, [recommendedCategory]);

  const handleRecommendation = () => {
    if (isEdit) return;
    setFirstClickCategory(true);
    const timeOutId = setTimeout(() => {
      const category = getRandomCategories();
      setRecommendedCategory(category);
    }, 1500);
    return () => {
      clearTimeout(timeOutId);
    };
  };

  const handleCategory = (categoryId: number) => {
    setCurrentCategoryId(categoryId);
    setItem((prev) => ({ ...prev, category: categoryId }));
  };

  const handlePrice = ({ target }: ChangeEvent<HTMLInputElement>) => {
    const { value } = target;
    const formattedPrice = getFormattedPrice(value);
    formattedPrice && setItem((prev) => ({ ...prev, price: formattedPrice }));
  };

  const handleContents = ({ target }: ChangeEvent<HTMLTextAreaElement>) => {
    const { value } = target;
    setItem((prev) => ({ ...prev, contents: value }));
  };

  useEffect(() => {
    const getCategories = async () => {
      const { data } = await api.get('/resources/categories');
      setRecommendedCategory(data.data.categories);
    };
    getCategories();
  }, []);

  useEffect(() => {
    if (isEdit && origin) {
      setItem({
        title: origin.title,
        contents: origin.contents,
        category: origin.category,
        price: origin.price,
        images: origin.images,
      });
      setFiles(
        origin.images.map((image: ImageFile) => ({
          preview: image.url,
        })),
      );
    }
  }, [isEdit, origin]);

  return (
    <MyNewContainer>
      <NavBar
        left={<button onClick={handleClose}>닫기</button>}
        center={'새 상품 등록'}
        right={<button onClick={handleSubmit}>완료</button>}
      />
      <MyNew>
        <MyImagesList>
          <MyImageBox>
            <Fileinput fileCount={`${files.length}/10`} onChage={handleFiles} />
          </MyImageBox>
          {files &&
            files.map((img, index) => (
              <li key={index}>
                <ImgBox
                  key={index}
                  src={img.preview}
                  alt={img.file?.name || `${index} 첨부파일`}
                  size="md"
                />
                {!index && <MyThumbnail>대표사진</MyThumbnail>}
                <Button
                  value={index}
                  icon
                  circle={'sm'}
                  onClick={handleDeleteFile}
                >
                  <Icon
                    name={'x'}
                    size={'xs'}
                    fill={palette.neutral.background}
                  />
                </Button>
              </li>
            ))}
        </MyImagesList>
        <MyTitleBox>
          <Textarea
            name={'title'}
            value={item.title}
            placeholder="글 제목"
            rows={item.title.length > 30 ? 2 : 1}
            maxLength={64}
            onChange={handleTitle}
            onClick={handleRecommendation}
          />
          {recommendedCategory.length === 3 && (
            <MyTitleCategories>
              <MyCategories>
                {recommendedCategory.map(({ id, title }) => {
                  const isActive = currentCategoryId === id;
                  return (
                    <Button
                      key={id}
                      active={isActive}
                      category
                      onClick={() => handleCategory(id)}
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
        <>
          <LabelInput
            label={'₩'}
            name={'price'}
            value={item.price}
            placeholder={'가격(선택사항)'}
            onChange={handlePrice}
          />
        </>
        <>
          <Textarea
            name={'contents'}
            value={item.contents}
            placeholder={`${userRegion}에 올릴 게시물 내용을 작성해주세요.`}
            onChange={handleContents}
          />
        </>
      </MyNew>
      <SubTabBar icon={'location'} content={`${userRegion}`}>
        <Icon name="keyboard" />
      </SubTabBar>
    </MyNewContainer>
  );
};

const slideInAnimation = keyframes`
  from {
    transform: translateY(100%);
  }
  to {
    transform: translateY(0);
  }
`;

const MyNewContainer = styled.div`
  position: absolute;
  bottom: 0;
  width: 100vw;
  height: 95vh;
  background-color: ${({ theme }) => theme.colors.neutral.background};
  color: ${({ theme }) => theme.colors.neutral.text};
  animation: ${slideInAnimation} 0.3s ease-in-out;
  border-radius: 10px 10px 0px 0px;
  > div:nth-child(1) {
    border-radius: 10px 10px 0px 0px;
  }
  input {
    color: ${({ theme }) => theme.colors.neutral.text};
  }
`;

const MyNew = styled.div`
  width: 100vw;
  height: calc(90vh - 85px);
  padding: 0 2.7vw;

  > div:nth-child(1) {
    padding: 15px 0;
  }
  > div:last-child {
    > textarea {
      /* TODO: 최대높이 반응형으로 조절해야됨 */
      max-height: 43vh;
      overflow: auto;
    }
  }
`;

const MyImagesList = styled.ul`
  display: flex;
  overflow-x: auto;
  -ms-overflow-style: none;
  padding: 16px 0;
  gap: 16px;
  border-bottom: 1px solid ${({ theme }) => theme.colors.neutral.border};
  > li {
    position: relative;
    > button {
      position: absolute;
      top: -5px;
      right: -5px;
      background-color: ${({ theme }) => theme.colors.neutral.textStrong};
      > svg {
        position: absolute;
      }
    }
  }
`;

const MyImageBox = styled.li`
  width: 80px;
  height: 80px;
  border-radius: 10px;
  border: 1px solid ${({ theme }) => theme.colors.neutral.border};
  object-fit: cover;
  > div {
    width: 80px;
  }
`;

const MyThumbnail = styled.div`
  position: absolute;
  width: 100%;
  bottom: 5px;
  padding: 4px 8px;
  border-radius: 0 0 10px 10px;
  background-color: ${({ theme }) => theme.colors.neutral.overlay};
  color: ${({ theme }) => theme.colors.accent.text};
  ${({ theme }) => theme.fonts.caption2};
  text-align: center;
`;

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

export default NewItemEditor;
