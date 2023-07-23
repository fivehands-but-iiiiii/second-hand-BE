export const getFormattedPrice = (price: number | null) =>
  price ? `${price.toLocaleString()}원` : '가격없음';
