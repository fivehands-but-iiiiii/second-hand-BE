export const getFormattedId = (input: string) => {
  const regExp = /[^0-9a-z]/;
  if (!regExp.test(input)) return;
  return input && input.replace(regExp, '');
};

export const getFormattedPrice = (input: string): string => {
  const numericValue = Number(input.replace(/\D/g, ''));
  const formattedPrice = numericValue.toLocaleString();
  return formattedPrice;
};

export const getFormattedNumber = (won: string): number => {
  const noPricePattern = /가격없음/i;
  if (noPricePattern.test(won)) return 0;
  const numericValue = parseInt(won.replace(/,/g, ''), 10);
  return numericValue;
};
