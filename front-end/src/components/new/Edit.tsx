import { useEffect, useState } from 'react';

import PortalLayout from '@components/layout/PortalLayout';
import NewItemEditor, { ItemInfo } from '@components/new/NewItemEditor';
import useAPI from '@hooks/useAPI';
import { getStoredValue } from '@utils/sessionStorage';
interface NewProps {
  id: string;
  onClick: () => void;
}

const Edit = ({ id, onClick }: NewProps) => {
  const { response, request } = useAPI();
  const [origin, setOrigin] = useState<ItemInfo>();
  // TODO: 유저지역정보가져오기 (로직 확인 필요함)
  const userInfo = getStoredValue({ key: 'userInfo' });
  const userRegion = userInfo.regions.filter(
    (region: string) => region.onFocus)[0];

  request({
    url: `/items/${id}`,
    method: 'get',
  });

  useEffect(() => {
    if (response) {
      setOrigin({
        id: response.data.id,
        title: response.data.title,
        contents: response.data.contents,
        region: userRegion,
        category: response.data.category,
        price: response.data.price,
        images: response.data.images,
      });
    }
  }, [response]);

  return (
    <PortalLayout>
      <NewItemEditor isEdit origin={origin} handleClose={onClick} />
    </PortalLayout>
  );
};

export default Edit;
