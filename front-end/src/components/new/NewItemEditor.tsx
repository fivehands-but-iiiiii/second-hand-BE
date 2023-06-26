import {
  useEffect,
  useState,
  ChangeEvent,
  MouseEvent,
  useCallback,
} from 'react';

import Icon from '@assets/Icon';
import LabelInput from '@common/LabelInput';
import NavBar from '@common/NavBar';
import SubTabBar from '@common/TabBar/SubTabBar';
import Textarea from '@common/Textarea/Textarea';
import { InputFile } from '@components/login/Join';
import { getPreviewURL } from '@utils/convertFile';
import { getFormattedPrice } from '@utils/formatText';

import { styled, keyframes } from 'styled-components';

import api from '../../api';

import ImageEditor from './itemEditor/ImageEditor';
import TitleEditor from './itemEditor/TitleEditor';

export interface Category {
  id: number;
  title: string;
}

export interface CategoryInfo {
  categories: Category[];
  recommendedCategory: Category[];
  currentId: number;
}

interface ImageFile {
  order: number;
  url: string;
}

export interface ItemInfo {
  id?: number;
  title: string;
  contents: string;
  region: number;
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
  const [firstClickCTitle, setFirstClickCTitle] = useState(false);
  const [categoryInfo, setCategoryInfo] = useState<CategoryInfo>({
    categories: [],
    recommendedCategory: [],
    currentId: 0,
  });
  const [files, setFiles] = useState<InputFile[]>([]);
  const [item, setItem] = useState<ItemInfo>({
    title: '',
    contents: '',
    region: 0,
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
    // formData.append('region', item.region.toString());
    // try {
    //   await api.post('/items', formData, {
    //     headers: {
    //       'Content-Type': 'multipart/form-data',
    //     },
    //   });
    // } catch (error) {
    //   console.log(error);
    // }
    // handleClose();
    // console.log(item);
    // console.log(files);
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
      const recommendedCategory = categoryInfo.recommendedCategory;
      const randomIndex = Math.floor(
        Math.random() * recommendedCategory.length,
      );
      randomCategories.add(recommendedCategory[randomIndex]);
    }
    const titleCategories = [...randomCategories];
    return titleCategories;
  }, [categoryInfo.recommendedCategory]);

  const handleRecommendation = () => {
    if (isEdit) return;
    setFirstClickCTitle(true);
    const timeOutId = setTimeout(() => {
      const category = getRandomCategories();
      setCategoryInfo((prev) => ({
        ...prev,
        recommendedCategory: category,
      }));
    }, 1500);
    return () => {
      clearTimeout(timeOutId);
    };
  };

  const handleCategory = (categoryId: number) => {
    setCategoryInfo((prev) => ({
      ...prev,
      currentId: categoryId,
    }));
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
      setCategoryInfo((prev) => ({
        ...prev,
        categories: data.data.categories,
      }));
    };
    getCategories();
  }, []);

  useEffect(() => {
    if (isEdit && origin) {
      setItem({
        title: origin.title,
        contents: origin.contents,
        category: origin.category,
        region: origin.region,
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
        <ImageEditor
          files={files}
          onChage={handleFiles}
          onClick={handleDeleteFile}
        />
        <TitleEditor
          title={item.title}
          categoryInfo={categoryInfo}
          onChageTitle={handleTitle}
          onClickTitle={handleRecommendation}
          onClickCategory={handleCategory}
        />
        <LabelInput
          label={'₩'}
          name={'price'}
          value={item.price}
          placeholder={'가격(선택사항)'}
          onChange={handlePrice}
        />
        <Textarea
          name={'contents'}
          value={item.contents}
          placeholder={`${item.region}에 올릴 게시물 내용을 작성해주세요.`}
          onChange={handleContents}
        />
      </MyNew>
      <SubTabBar icon={'location'} content={`${item.region}`}>
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

export default NewItemEditor;
