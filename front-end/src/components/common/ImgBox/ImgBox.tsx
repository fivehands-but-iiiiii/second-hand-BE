import { ComponentPropsWithRef } from 'react';

import { styled } from 'styled-components';

interface ImgBoxProps extends ComponentPropsWithRef<'img'> {
  src: string;
  alt: string;
  size?: 'sm' | 'md' | 'lg';
}

interface ImgBoxStyleProps {
  boxSize: number;
}

const ImgBox = ({ src, alt, size = 'lg' }: ImgBoxProps) => {
  const boxType = {
    sm: 48,
    md: 80,
    lg: 120,
  };
  const boxSize = boxType[size];
  return (
    <MyImgBox boxSize={boxSize}>
      <MyImg src={src} alt={alt} />
    </MyImgBox>
  );
};

const MyImgBox = styled.div<ImgBoxStyleProps>`
  width: ${({ boxSize }) => boxSize}px;
  height: ${({ boxSize }) => boxSize}px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  background-color: #f2f2f2;
  border: 1px solid ${({ theme }) => theme.colors.neutral.border};
`;

const MyImg = styled.img`
  width: inherit;
  height: inherit;
  border-radius: 10px;
  object-fit: cover;
`;

export default ImgBox;
