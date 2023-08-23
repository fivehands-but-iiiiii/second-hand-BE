import { useEffect, useState, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';

import Icon from '@assets/Icon';
import Alert from '@common/Alert';
import {
  ALERT_ACTIONS,
  ALERT_TITLE,
  AlertActionsProps,
} from '@common/Alert/constants';
import Button from '@common/Button';
import { SaleItem } from '@common/Item';
import NavBar from '@common/NavBar';
import PopupSheet from '@common/PopupSheet';
import { REGION_MENU } from '@common/PopupSheet/constants';
import Spinner from '@common/Spinner/Spinner';
import Category from '@components/home/category';
import ItemList from '@components/home/ItemList';
import { useCategories } from '@components/layout/MobileLayout';
import { RegionInfo } from '@components/login/Join';
import New from '@components/new/New';
import SettingRegionMap from '@components/region/SettingRegionMap';
import useIntersectionObserver from '@hooks/useIntersectionObserver';
import palette from '@styles/colors';
import { getStoredValue, setStorageValue } from '@utils/sessionStorage';

import { styled } from 'styled-components';

import api from '../api';

import ItemDetail from './ItemDetail';

interface HomeInfo {
  page: number;
  hasPrevious: boolean;
  hasNext: boolean;
  items: SaleItem[];
}

interface HomeFilterInfo {
  regionId: number;
  isSales: boolean | null;
  categoryId: number | null;
}

export type HomePageInfo = Omit<HomeInfo, 'items'>;

const Home = () => {
  const navigator = useNavigate();
  const userInfo = getStoredValue({ key: 'userInfo' });
  const userRegion = userInfo?.regions;
  const [userRegions, setUserRegions] = useState<RegionInfo[]>(
    userRegion || [
      {
        id: 1168064000,
        district: '역삼1동',
        onFocus: true,
      },
    ],
  );
  const currentRegion = userRegions.find(({ onFocus }) => onFocus);
  if (!currentRegion) return;
  const currentRegionId = currentRegion?.id;
  const categories = useCategories();
  const [saleItems, setSaleItems] = useState<SaleItem[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [selectedItem, setSelectedItem] = useState(0);
  const [isCategoryModalOpen, setIsCategoryModalOpen] = useState(false);
  const [isNewModalOpen, setIsNewModalOpen] = useState(false);
  const [isRegionPopupSheetOpen, setIsRegionPopupSheetOpen] = useState(false);
  const [isRegionMapModalOpen, setIsRegionMapModalOpen] = useState(false);
  const [isLoginAlertOpen, setIsLoginAlertOpen] = useState(false);
  const [filterInfo, setFilterInfo] = useState<HomeFilterInfo>({
    regionId: currentRegionId,
    isSales: null,
    categoryId: null,
  });
  const [homePageInfo, setHomePageInfo] = useState<HomePageInfo>({
    page: 0,
    hasPrevious: false,
    hasNext: true,
  });

  const onIntersect: IntersectionObserverCallback = ([{ isIntersecting }]) => {
    if (isIntersecting && !isLoading) fetchItems();
  };

  const { setTarget } = useIntersectionObserver({ onIntersect });

  const createFilterQuery = () => {
    const { regionId, isSales, categoryId } = filterInfo;
    const salesQuery = isSales ? `&isSales=${isSales}` : '';
    const categoryQuery = categoryId ? `&categoryId=${categoryId}` : '';
    const filterQuery = `?page=${homePageInfo.page}&regionId=${regionId}${salesQuery}${categoryQuery}`;
    return filterQuery;
  };

  const initData = () => {
    setSaleItems([]);
    setHomePageInfo({
      page: 0,
      hasPrevious: false,
      hasNext: true,
    });
  };

  const patchUserRegion = async (regionId: number) => {
    try {
      const updatedRegions = userRegions.map((region) => {
        return region.id === regionId
          ? {
              ...region,
              onFocus: true,
            }
          : {
              ...region,
              onFocus: false,
            };
      });
      const { data } = await api.patch('/members/region', {
        id: userInfo?.id,
        regions: updatedRegions,
      });
      const updatedUserAccount = { ...userInfo, regions: updatedRegions };
      if (data) {
        setUserRegions(updatedRegions);
        setFilterInfo((prevFilterInfo) => ({
          ...prevFilterInfo,
          regionId: regionId,
        }));
        setStorageValue({ key: 'userInfo', value: updatedUserAccount });
      }
      return data.data;
    } catch (error) {
      console.error(error);
    }
  };

  const handleRegionSwitch = async (regionId: number) => {
    const patchResult = await patchUserRegion(regionId);
    if (patchResult) {
      setHomePageInfo({
        page: 0,
        hasPrevious: false,
        hasNext: true,
      });
      setFilterInfo((prevFilterInfo) => ({
        ...prevFilterInfo,
        regionId: regionId,
      }));
      setSaleItems([]);
      setIsRegionPopupSheetOpen((prev) => !prev);
    }
  };

  const handleRegionPopupSheetModal = () =>
    setIsRegionPopupSheetOpen((prev) => !prev);

  const handleRegionMapModal = () => {
    setIsRegionMapModalOpen((prev) => !prev);
    setIsRegionPopupSheetOpen(false);
    if (!isRegionMapModalOpen) return;
    const userInfo = getStoredValue({ key: 'userInfo' });
    const userRegion: RegionInfo[] = userInfo?.regions;
    const currentRegion = userRegion.find(({ onFocus }) => onFocus);
    if (!currentRegion) return;
    initData();
    setUserRegions(userRegion);
    setFilterInfo((prevFilterInfo) => ({
      ...prevFilterInfo,
      regionId: currentRegion.id,
    }));
  };

  const handleCategoryModal = () => setIsCategoryModalOpen((prev) => !prev);

  const regionPopupSheetMenu = useMemo(() => {
    if (!userInfo)
      return userRegions.map(({ id, district, onFocus }) => ({
        id,
        title: district,
        style: onFocus ? 'font-weight: 600' : '',
        onClick: () => handleRegionSwitch(id),
      }));
    else
      return [
        ...userRegions.map(({ id, district, onFocus }) => {
          return {
            id,
            title: district,
            style: onFocus ? 'font-weight: 600' : '',
            onClick: () => handleRegionSwitch(id),
          };
        }),
        ...REGION_MENU.map(({ id, title }) => {
          return {
            id,
            title,
            onClick: handleRegionMapModal,
          };
        }),
      ];
  }, [userRegions]);

  const [onRefresh, setOnRefresh] = useState(false);

  const handleNewButtonClick = () => {
    if (!userInfo) {
      setIsLoginAlertOpen(true);
      return;
    }
    setIsNewModalOpen((prev) => !prev);
  };

  const handleAlert = (type: AlertActionsProps['id']) => {
    if (type !== 'home' && type !== 'login') return;
    const actions = {
      home: () => setIsLoginAlertOpen(false),
      login: () => navigator('/login'),
    };
    return actions[type]();
  };

  const alertButtons = (actions: AlertActionsProps[]) =>
    actions.map(({ id, action }) => (
      <button key={id} onClick={() => handleAlert(id)}>
        {action}
      </button>
    ));

  const handleNewModal = () => {
    setIsNewModalOpen((prev) => !prev);
    if (!isNewModalOpen) return;
    initData();
    setOnRefresh(true);
  };

  const handleFilterCategory = (categoryId: number) => {
    setHomePageInfo({
      page: 0,
      hasPrevious: false,
      hasNext: true,
    });
    setFilterInfo((prevFilterInfo) => ({
      ...prevFilterInfo,
      categoryId: categoryId,
    }));
    setSaleItems([]);
  };

  const handleItemDetail = (id: number) => {
    setSelectedItem(id);
    if (id) return;
    initData();
    setOnRefresh(true);
  };

  const fetchItems = async () => {
    if (!homePageInfo.hasNext) return;

    const filterQuery = createFilterQuery();

    try {
      setIsLoading(true);

      const { data } = await api.get(`items${filterQuery}`);
      setSaleItems((prevItems) => {
        const newSet = new Set(prevItems);
        data.data.items.forEach((item: SaleItem) => newSet.add(item));
        return [...newSet];
      });

      setHomePageInfo({
        page: data.number + 1,
        hasPrevious: data.hasPrevious,
        hasNext: data.hasNext,
      });
    } catch (error) {
      console.error(`Failed to fetch items: ${error}`);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchItems();
  }, [filterInfo]);

  useEffect(() => {
    if (onRefresh) {
      fetchItems();
      setOnRefresh(false);
    }
  }, [onRefresh]);

  return (
    <>
      <NavBar
        type="blur"
        left={
          <>
            <MyNavBarBtn onClick={handleRegionPopupSheetModal}>
              {currentRegion?.district}
              <Icon name={'chevronDown'} />
            </MyNavBarBtn>
            {isRegionPopupSheetOpen && (
              <PopupSheet
                type={'slideDown'}
                menu={regionPopupSheetMenu}
                onSheetClose={handleRegionPopupSheetModal}
              />
            )}
          </>
        }
        right={
          <button onClick={handleCategoryModal}>
            <Icon name={'hamburger'} />
          </button>
        }
      />
      {isRegionMapModalOpen && (
        <SettingRegionMap
          regions={userRegions}
          onPortal={handleRegionMapModal}
        />
      )}
      <ItemList saleItems={saleItems} onItemClick={handleItemDetail} />
      {!!saleItems.length && <MyOnFetchItems ref={setTarget}></MyOnFetchItems>}
      {isLoading && <Spinner />}
      {!!selectedItem && (
        <ItemDetail
          id={selectedItem}
          categoryInfo={categories}
          handleBackBtnClick={handleItemDetail}
        />
      )}
      {isCategoryModalOpen && (
        <Category
          categoryInfo={categories}
          handleCategoryModal={handleCategoryModal}
          onCategoryClick={handleFilterCategory}
        />
      )}
      <MyNewBtn active circle={'lg'} onClick={handleNewButtonClick}>
        <Icon name={'plus'} fill={palette.neutral.backgroundWeak} />
      </MyNewBtn>
      <Alert isOpen={isLoginAlertOpen}>
        <Alert.Title>{ALERT_TITLE.LOGIN}</Alert.Title>
        <Alert.Button>{alertButtons(ALERT_ACTIONS.LOGIN)}</Alert.Button>
      </Alert>
      {isNewModalOpen && (
        <New categoryInfo={categories} onClick={handleNewModal} />
      )}
    </>
  );
};

const MyNavBarBtn = styled.button`
  display: flex;
  gap: 5px;
`;

const MyOnFetchItems = styled.div`
  margin-bottom: 75px;
`;

const MyNewBtn = styled(Button)`
  position: fixed;
  right: 24px;
  bottom: 120px;
`;

export default Home;
