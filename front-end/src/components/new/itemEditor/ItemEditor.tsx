import {
  useEffect,
  useState,
  ChangeEvent,
  MouseEvent,
  useCallback,
  useRef,
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
  selectedId: number;
}

interface ImageFile {
  order: number;
  url: string;
}

export interface ItemInfo {
  id: number;
  title: string;
  contents: string;
  region: number;
  category: number;
  price: string;
  images: ImageFile[];
}

interface ItemEditorProps {
  categoryInfo: Category[];
  isEdit?: boolean;
  origin?: ItemInfo;
  handleClose: () => void;
}

const ItemEditor = ({
  categoryInfo,
  isEdit = false,
  handleClose,
}: ItemEditorProps) => {
  // 지역정보 가져오기
  const [title, setTitle] = useState('');
  const [firstClickCTitle, setFirstClickCTitle] = useState(false);
  const [contents, setContents] = useState('');
  const [region] = useState(2729060200); // TODO: 지역 유저정보에서 받아오기
  const [price, setPrice] = useState('');
  const priceRef = useRef<HTMLInputElement>(null);
  const [category, setCategory] = useState<CategoryInfo>({
    total: categoryInfo,
    recommendedCategory: [],
    selectedId: 0,
  });
  const [files, setFiles] = useState<InputFile[]>([]);
  const [isFormValid, setFormValid] = useState(true);

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

  const validateForm = useCallback(() => {
    if (!title || !category.selectedId || !region || !files.length)
      return false;
    else return true;
  }, [title, region, category, files]);

  // TODO: 수정 api 변경필요함
  const handleSubmit = async () => {
    if (!contents || !priceRef.current) return;
    const formData = new FormData();
    files.forEach(({ file }) => {
      formData.append('images', file as Blob, file?.name);
    });
    formData.append('title', title);
    formData.append('contents', contents);
    formData.append('category', category.selectedId.toString());
    formData.append(
      'price',
      parseInt(priceRef.current.value.replace(/,/g, '')).toString(),
    );
    formData.append('region', region.toString());
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
    setTitle(value);
  };

  const getRandomCategories = useCallback((): Category[] => {
    const RANDOM_COUNT = 3;
    const randomCategories: Set<Category> = new Set();
    while (randomCategories.size < RANDOM_COUNT) {
      const randomIndex = Math.floor(Math.random() * categoryInfo.length);
      randomCategories.add(categoryInfo[randomIndex]);
    }
    const titleCategories = [...randomCategories];
    return titleCategories;
  }, [categoryInfo]);

  const handleRecommendation = useCallback(() => {
    if (isEdit) return;
    if (firstClickCTitle) return;
    const timeOutId = setTimeout(() => {
      const category = getRandomCategories();
      setCategory((prev) => ({
        ...prev,
        recommendedCategory: category,
      }));
      setFirstClickCTitle(true);
    }, 1500);
    return () => {
      clearTimeout(timeOutId);
    };
  }, [firstClickCTitle, getRandomCategories]);

  // TODO: 랜덤 3개 추출하는 함수 (BE API나오면 제거예정)
  const handleCategory = (updatedCategory: Category) => {
    const isSameCategory = category.selectedId === updatedCategory.id;
    const isExistingCategory = category.recommendedCategory.some(
      ({ id }) => id === updatedCategory.id,
    );
    if (isSameCategory) {
      return setCategory((prev) => ({
        ...prev,
        selectedId: 0,
      }));
    } else if (isExistingCategory) {
      setCategory((prev) => ({
        ...prev,
        selectedId: updatedCategory.id,
      }));
    } else {
      const updatedRecommendedCategory = [
        ...[
          updatedCategory,
          ...category.recommendedCategory.filter(
            (category) => category.id !== updatedCategory.id,
          ),
        ],
      ].splice(0, 3);
      setCategory((prev) => ({
        ...prev,
        recommendedCategory: updatedRecommendedCategory,
        selectedId: updatedCategory.id,
      }));
    }
  };

  const handlePrice = ({ target }: ChangeEvent<HTMLInputElement>) => {
    const { value } = target;
    const formattedPrice = getFormattedPrice(value);
    setPrice(formattedPrice);
  };

  const handleContents = ({ target }: ChangeEvent<HTMLTextAreaElement>) => {
    const { value } = target;
    setContents(value);
  };

  useEffect(() => {
    setFormValid(validateForm());
  }, [title, region, category, files, validateForm]);

  return (
    <>
      <NavBar
        left={<button onClick={handleClose}>닫기</button>}
        center={'새 상품 등록'}
        right={
          <button disabled={!isFormValid} onClick={handleSubmit}>
            완료
          </button>
        }
      />
      <MyNew>
        <ImageEditor
          files={files}
          onChage={handleFiles}
          onClick={handleDeleteFile}
        />
        <TitleEditor
          title={title}
          category={category}
          onChageTitle={handleTitle}
          onClickTitle={handleRecommendation}
          onClickCategory={handleCategory}
        />
        <MyPrice
          label={'₩'}
          name={'price'}
          value={price}
          maxLength={20}
          placeholder={'가격(선택사항)'}
          onChange={handlePrice}
          ref={priceRef}
        />
        <MyContents
          name={'contents'}
          value={contents}
          placeholder={`${region}에 올릴 게시물 내용을 작성해주세요.`}
          onChange={handleContents}
        />
      </MyNew>
      <SubTabBar icon={'location'} content={`${region}`}>
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
`;

const MyPrice = styled(LabelInput)`
  padding: 15px 15px 15px 0;
  line-height: 1.5;
  label {
    padding-left: 15px;
    ${({ theme }) => theme.colors.neutral.border};
  }
`;

const MyContents = styled(Textarea)`
  > textarea {
    /* TODO: 최대높이 반응형으로 조절해야됨 */
    height: 52vh;
    overflow: auto;
    -ms-overflow-style: none;
    &::-webkit-scrollbar {
      display: none;
    }
  }
`;

export default ItemEditor;
