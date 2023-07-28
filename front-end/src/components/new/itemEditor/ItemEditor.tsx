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
import { InputFile, RegionInfo } from '@components/login/Join';
import useAPI from '@hooks/useAPI';
import { getPreviewURL } from '@utils/convertFile';
import { getFormattedPrice, getFormattedNumber } from '@utils/formatText';
import { getStoredValue } from '@utils/sessionStorage';

import { styled } from 'styled-components';

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

export interface ItemInfo {
  id: number;
  title: string;
  contents: string;
  region: number;
  category: number;
  price: string;
  images: string[];
}

export interface OriginItem {
  id: number;
  title: string;
  contents: string;
  category: string | number;
  price: string;
  images: { url: string }[];
}

interface ItemEditorProps {
  categoryInfo: Category[];
  isEdit?: boolean;
  origin?: OriginItem;
  handleClose: () => void;
}

const ItemEditor = ({
  categoryInfo,
  isEdit = false,
  origin,
  handleClose,
}: ItemEditorProps) => {
  const pageTitle = isEdit ? '상품 수정' : '새 상품 등록';
  const userInfo = getStoredValue({ key: 'userInfo' });
  const region = userInfo?.regions.find(({ onFocus }: RegionInfo) => onFocus);
  if (!region) return;
  const [title, setTitle] = useState('');
  const [firstClickCTitle, setFirstClickCTitle] = useState(false);
  const [contents, setContents] = useState('');
  const [price, setPrice] = useState('');
  const priceRef = useRef<HTMLInputElement>(null);
  const [category, setCategory] = useState<CategoryInfo>({
    total: categoryInfo,
    recommendedCategory: [],
    selectedId: 0,
  });
  const [files, setFiles] = useState<InputFile[]>([]);
  const [isFormValid, setFormValid] = useState(true);
  const { request } = useAPI();

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
    return title && category.selectedId && region.id && files.length;
  }, [title, region, category, files]);

  // TODO: 전송 실패 처리
  const handleSubmit = async () => {
    try {
      if (isEdit) {
        await putEdit();
        handleClose();
        return;
      }
      await postNew();
      handleClose();
    } catch (error) {
      console.error('error');
    }
  };

  const putEdit = async () => {
    if (!contents || !priceRef.current) return;
    try {
      const newFiles = await editFiles();
      if (!newFiles) return;
      const newImages = [
        ...files
          .filter(({ preview, file }) => (!file ? preview : null))
          .map(({ preview }) => ({ url: preview })),
        ...newFiles.map((image) => ({ url: image })),
      ];
      const editedData = {
        title: title,
        contents: contents,
        category: category.selectedId,
        region: region.id,
        price: parseInt(priceRef.current.value.replace(/,/g, '')),
        images: newImages,
        firstImageUrl: newImages && newImages[0],
      };
      await request({
        url: `/items/${origin?.id}`,
        method: 'put',
        config: {
          data: editedData,
        },
      });
    } catch (error) {
      console.log(error);
    }
  };

  const editFiles = async () => {
    try {
      const newImages = await Promise.all(
        files
          .filter(({ file }) => file)
          .map(async ({ file }) => {
            const formData = new FormData();
            formData.append('itemImages', file as Blob, file?.name);
            const { data } = await request({
              url: '/items/image',
              method: 'post',
              config: {
                data: formData,
                headers: {
                  'Content-Type': 'multipart/form-data',
                },
              },
            });
            return data.imageUrl;
          }),
      );
      return newImages;
    } catch (error) {
      console.log(error);
    }
  };

  const postNew = async () => {
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
    formData.append('region', region.id.toString());
    try {
      await request({
        url: '/items',
        method: 'post',
        config: {
          data: formData,
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        },
      });
    } catch (error) {
      console.log(error);
    }
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
    const usedId = new Set();
    const randomCategories: Category[] = [];
    if (isEdit && origin) {
      const categoryId = categoryInfo.find(
        (item) => item.title === origin.category,
      )?.id as number;
      randomCategories.push({
        id: categoryId,
        title: origin.category as string,
      });
      usedId.add(categoryId);
    }
    while (randomCategories.length < RANDOM_COUNT) {
      const randomIndex = Math.floor(Math.random() * categoryInfo.length);
      if (!usedId.has(randomIndex))
        randomCategories.push(categoryInfo[randomIndex]);
    }
    return randomCategories;
  }, [categoryInfo]);

  const handleRecommendation = useCallback(() => {
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

  // TODO: 랜덤 3개 추출하는 함수 (중복 절대 불가!!)
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

  const getMappedOrigin = (origin: OriginItem) => {
    if (!origin) return;
    const formattedPrice = getFormattedNumber(origin.price);
    const categoryId = categoryInfo.find(
      (item) => item.title === origin.category,
    );
    const mappedOrigin = {
      ...origin,
      price: formattedPrice.toString(),
      category: categoryId?.id,
    };
    setTitle(mappedOrigin.title);
    setContents(mappedOrigin.contents);
    setPrice(mappedOrigin.price);
    setCategory((prev) => ({
      ...prev,
      selectedId: mappedOrigin.category as number,
    }));
    setFiles(
      mappedOrigin.images.map((image) => ({
        preview: image.url,
      })),
    );
  };

  useEffect(() => {
    setFormValid(validateForm());
  }, [title, region, category, files, validateForm]);

  useEffect(() => {
    if (isEdit && origin) {
      getMappedOrigin(origin);
      const category = getRandomCategories();
      setCategory((prev) => ({
        ...prev,
        recommendedCategory: category,
      }));
    }
  }, [isEdit, origin]);

  return (
    <>
      <NavBar
        left={<button onClick={handleClose}>닫기</button>}
        center={pageTitle}
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
        <Textarea
          name={'contents'}
          value={contents}
          placeholder={`${region.district}에 올릴 게시물 내용을 작성해주세요.`}
          onChange={handleContents}
        />
      </MyNew>
      <SubTabBar
        icon={'location'}
        content={`${region.district}`}
      >
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
    padding-top: 10px;
    max-height: 40vh;
    > textarea {
      max-height: 40vh;
      overflow: auto;
    }
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

export default ItemEditor;
