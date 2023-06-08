import { styled } from 'styled-components';

import theme from '../../../styles/theme';

export interface ImgBoxProps {
  src: string;
  width: number;
  height: number;
}

const ImgBox = ({ src, width, height }: ImgBoxProps) => {
  return <ImgBoxContainer src={src} alt={src} width={width} height={height} />;
};

ImgBox.defaultProps = {
  width: 120,
  height: 120,
};

const ImgBoxContainer = styled.img<ImgBoxProps>`
  width: ${({ width }) => width}px;
  height: ${({ height }) => height}px;
  border: 5px solid ${theme.neutral.border};
  border-radius: 10px;
  background-color: #f2f2f2;
`;

export default ImgBox;
