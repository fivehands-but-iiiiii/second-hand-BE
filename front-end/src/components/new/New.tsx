import { CategoryInfo } from '@components/home/category';
import PortalLayout from '@components/layout/PortalLayout';
import ItemEditor, { OriginItem } from '@components/new/itemEditor/ItemEditor';
interface NewProps {
  categoryInfo: CategoryInfo[];
  isEdit?: boolean;
  origin?: OriginItem;
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
