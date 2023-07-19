import { CategoryInfo } from '@components/home/category';
import PortalLayout from '@components/layout/PortalLayout';
import ItemEditor from '@components/new/itemEditor/ItemEditor';

interface NewProps {
  region: {
    id: number;
    district: string;
    onFocus: boolean;
  };
  categoryInfo: CategoryInfo[];
  onClick: () => void;
  handleRegion: () => void;
}

// TODO: 지도에서 지역설정 달라지면 등록하는 지역도 달라져야 함
const New = ({ region, categoryInfo, onClick, handleRegion }: NewProps) => {
  // TODO: isEdit, origin 여부 확인하고 에디터에 데이터 넣음
  return (
    <PortalLayout>
      <ItemEditor
        region={region}
        categoryInfo={categoryInfo}
        handleClose={onClick}
        handleRegion={handleRegion}
      />
    </PortalLayout>
  );
};

export default New;
