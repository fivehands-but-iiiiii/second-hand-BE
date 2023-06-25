export const getFormattedId = (input: string) => {
  const regExp = /[^0-9a-z]/;
  if (!regExp.test(input)) return;
  return input && input.replace(regExp, '');
};

export const getFormattedPrice = (input: string) => {
  const regExp = /\D/g;
  if (!regExp.test(input)) return;
  return (
    input && input.replace(regExp, '').replace(/\B(?=(\d{3})+(?!\d))/g, ',')
  );
};
