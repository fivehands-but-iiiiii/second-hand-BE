import { CategoryInfo } from '@components/home/category';
import PortalLayout from '@components/layout/PortalLayout';
import ItemEditor, { OriginItem } from '@components/new/itemEditor/ItemEditor';
interface NewProps {
  isEdit?: boolean;
  origin?: OriginItem;
  categoryInfo: CategoryInfo[];
  onClick: () => void;
}

const New = ({ categoryInfo, onClick, ...editProps }: NewProps) => {
  return (
    <PortalLayout>
      <ItemEditor
        categoryInfo={categoryInfo}
        handleClose={onClick}
        {...editProps}
      />
    </PortalLayout>
  );
};

export default New;
