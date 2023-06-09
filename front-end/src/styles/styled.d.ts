import 'styled-components';
import { ColorsType, FontsType } from './theme';

declare module 'styled-components' {
  export interface DefaultTheme {
    colors: ColorsType;
    fonts: FontsType;
  }
}
