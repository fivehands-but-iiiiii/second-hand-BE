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
import Textarea from '@common/Textarea';
import { InputFile } from '@components/login/Join';
import { getPreviewURL } from '@utils/convertFile';
import { getFormattedPrice } from '@utils/formatText';

import { styled } from 'styled-components';

import api from '../../../api';
import ImageEditor from '../itemEditor/ImageEditor';
import TitleEditor from '../itemEditor/TitleEditor';

export interface Category {
  id: number;
  title: string;
}

export interface CategoryInfo {
  total: Category[];
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

interface InputFile {
  preview: string;
  file?: File;
}

interface ItemEditorProps {
  isEdit?: boolean;
  origin?: ItemInfo;
  handleClose: () => void;
}

const ItemEditor = ({
  isEdit = false,
  origin,
  handleClose,
}: ItemEditorProps) => {
  // 지역정보 가져오기
  const [firstClickCTitle, setFirstClickCTitle] = useState(false);
  const [categoryInfo, setCategoryInfo] = useState<CategoryInfo>({
    total: [],
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

  // TODO: 새상품등록, 수정 api 변경필요함
  const handleSubmit = async () => {
    const formData = new FormData();
    files.forEach(({ file }) => {
      formData.append('images', file as Blob, file?.name);
    });
    formData.append('title', item.title);
    formData.append('contents', item.contents);
    formData.append('category', item.category.toString());
    formData.append('price', item.price.toString());
    formData.append('region', item.region.toString());
    try {
      await api.post('/items', formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
      });
    } catch (error) {
      console.log(error);
    }
    handleClose();
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
      const categories = categoryInfo.total;
      const randomIndex = Math.floor(Math.random() * categories.length);
      randomCategories.add(categories[randomIndex]);
    }
    const titleCategories = [...randomCategories];
    return titleCategories;
  }, [categoryInfo.total]);

  const handleRecommendation = useCallback(() => {
    if (isEdit) return;
    if (firstClickCTitle) return;
    const timeOutId = setTimeout(() => {
      const category = getRandomCategories();
      setCategoryInfo((prev) => ({
        ...prev,
        recommendedCategory: category,
      }));
      setFirstClickCTitle(true);
    }, 1500);
    return () => {
      clearTimeout(timeOutId);
    };
  }, [firstClickCTitle, getRandomCategories]);

  const handleCategory = (updatedCategory: Category) => {
    if (updatedCategory.id === categoryInfo.currentId) {
      return setCategoryInfo((prev) => ({
        ...prev,
        currentId: 0,
      }));
    } else if (
      categoryInfo.recommendedCategory.some(({ id }) => {
        return id === updatedCategory.id;
      })
    ) {
      setCategoryInfo((prev) => ({
        ...prev,
        currentId: updatedCategory.id,
      }));
    } else {
      const updatedRecommendedCategory = [
        ...[
          updatedCategory,
          ...categoryInfo.recommendedCategory.filter(
            (category) => category.id !== updatedCategory.id,
          ),
        ],
      ].splice(0, 3);
      setCategoryInfo((prev) => ({
        ...prev,
        recommendedCategory: updatedRecommendedCategory,
        currentId: updatedCategory.id,
      }));
    }
    setItem((prev) => ({ ...prev, category: updatedCategory.id }));
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

  useEffect(() => {
    const getCategories = async () => {
      const { data } = await api.get('/resources/categories');
      setCategoryInfo((prev) => ({
        ...prev,
        total: data.data.categories,
      }));
    };
    getCategories();
  }, []);

  return (
    <>
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
    </>
  );
};

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
      height: 52vh;
      overflow: auto;
      -ms-overflow-style: none;
      &::-webkit-scrollbar {
        display: none;
      }
    }
  }
`;

export default ItemEditor;
