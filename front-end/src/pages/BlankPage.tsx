import Icon from '@assets/Icon';

import { styled } from 'styled-components';

interface BlankPageProps {
  title: string;
}

const BlankPage = ({ title }: BlankPageProps) => {
  return (
    <MyBlankPage>
      <span> {title}이 없습니다 </span>
      <Icon name={'carrot'} size={'sm'} />
    </MyBlankPage>
  );
};

const MyBlankPage = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  height: calc(90vh - 83px);
  color: ${({ theme }) => theme.colors.neutral.textWeak};
`;

export default BlankPage;
