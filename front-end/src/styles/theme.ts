import { DefaultTheme } from 'styled-components';
import palette from './colors';
import fonts from './fonts';

export type ColorsType = typeof palette;
export type FontsType = typeof fonts;

const theme: DefaultTheme = {
  colors: palette,
  fonts: fonts,
};

export default theme;
