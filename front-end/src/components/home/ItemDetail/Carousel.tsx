import Slider from 'react-slick';

import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import { ItemImages } from '@pages/ItemDetail';

import { styled } from 'styled-components';

interface CarouselProps {
  itemTitle: string;
  images: ItemImages[];
}

const Carousel = ({ itemTitle, images }: CarouselProps) => {
  const imageInfo = images.map((image, index) => ({
    id: image.order,
    url: image.url,
    alt: `${itemTitle}-${index}`,
  }));

  const settings = {
    dots: true,
    className: 'center',
    infinite: true,
    centerPadding: '60px',
    slidesToShow: 1,
    swipeToSlide: true,
    arrows: false,
  };

  if (!images.length) return;

  return (
    <MyContainer>
      <Slider {...settings}>
        {imageInfo.map((image) => (
          <MyImgContainer key={image.id}>
            <MyCarouselImg src={image.url} alt={image.alt} />
          </MyImgContainer>
        ))}
      </Slider>
    </MyContainer>
  );
};

const MyContainer = styled.div`
  & .slick-slide div {
    display: flex !important;
  }
  & .slick-dots {
    position: absolute;
    top: 450px;
  }
`;

const MyImgContainer = styled.div`
  height: 491px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 0;
  margin-bottom: 20px;
`;

const MyCarouselImg = styled.img`
  height: 419px;
  object-fit: cover;
`;

export default Carousel;
