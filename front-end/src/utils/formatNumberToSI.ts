export const formatNumberToSI = (num: number, digits = 1): string => {
  const si = [
    { value: 1, symbol: '' },
    { value: 1e3, symbol: 'k' },
    { value: 1e6, symbol: 'M' },
    { value: 1e9, symbol: 'G' },
    { value: 1e12, symbol: 'T' },
    { value: 1e15, symbol: 'P' },
    { value: 1e18, symbol: 'E' },
  ];

  const index = si.findIndex((si) => num < si.value) - 1;

  return Number((num / si[index].value).toFixed(digits)) + si[index].symbol;
};
