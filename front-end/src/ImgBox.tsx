import { styled } from 'styled-components';

export interface ImgBoxProps {
  src: string;
  width: number;
  height: number;
}

const ImgBox = ({ src, width, height }: ImgBoxProps) => {
  return <ImgBoxContainer src={src} alt={src} width={width} height={height} />;
};

const ImgBoxContainer = styled.img<ImgBoxProps>`
  width: ${({ width }) => width}px;
  height: ${({ height }) => height}px;
  border: 1px solid #ccc;
  border-radius: 10px;
  background-color: #f2f2f2;
`;

ImgBox.defaultProps = {
  width: 120,
  height: 120,
};

export default ImgBox;
