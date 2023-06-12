import { styled } from 'styled-components';

export interface ImgBoxProps {
  src: string;
  size?: 'sm' | 'md' | 'lg';
}

const ImgBox = ({ src, size = 'lg' }: ImgBoxProps) => {
  const boxType = {
    sm: 48,
    md: 80,
    lg: 120,
  };
  const boxSize = boxType[size];
  return <MyImgBox src={src} alt={src} width={boxSize} height={boxSize} />;
};

const MyImgBox = styled.img<ImgBoxProps>`
  border-radius: 10px;
  background-color: #f2f2f2;
  border: 1px solid ${({ theme }) => theme.colors.neutral.border};
`;

export default ImgBox;
