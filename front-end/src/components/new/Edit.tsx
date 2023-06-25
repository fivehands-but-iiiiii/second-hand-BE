import { useEffect, useState } from 'react';

import PortalLayout from '@components/layout/PortalLayout';
import NewItemEditor, { ItemInfo } from '@components/new/NewItemEditor';
import useAPI from '@hooks/useAPI';

interface NewProps {
  id: string;
  onClick: () => void;
}

const Edit = ({ id, onClick }: NewProps) => {
  const { response, request } = useAPI();
  const [origin, setOrigin] = useState<ItemInfo>();

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
