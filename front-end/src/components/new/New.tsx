import PortalLayout from '@components/layout/PortalLayout';
import NewItemEditor from '@components/new/NewItemEditor';

interface NewProps {
  onClick: () => void;
}

const New = ({ onClick }: NewProps) => {
  return (
    <PortalLayout>
      <NewItemEditor handleClose={onClick} />
    </PortalLayout>
  );
};

export default New;
