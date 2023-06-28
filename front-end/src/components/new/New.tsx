import Category from '@components/home/category';
import PortalLayout from '@components/layout/PortalLayout';
import ItemEditor from '@components/new/itemEditor/ItemEditor';
interface NewProps {
  categoryInfo: Category[];
  onClick: () => void;
}

const New = ({ categoryInfo, onClick }: NewProps) => {
  // TODO: isEdit, origin 여부 확인하고 에디터에 데이터 넣음

  return (
    <PortalLayout>
      <ItemEditor categoryInfo={categoryInfo} handleClose={onClick} />
    </PortalLayout>
  );
};

export default New;
