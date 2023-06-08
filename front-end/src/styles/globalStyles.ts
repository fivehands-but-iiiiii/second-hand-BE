import { createGlobalStyle } from 'styled-components';
import { normalize } from 'styled-normalize';

const GlobalStyle = createGlobalStyle`
  ${normalize}

  html,
  body {
    overflow: hidden;
    background-color: tomato;
  }

  // TODO: Page Layout 생성 후 속성 이동
  .app {
    width: 393px;
    height: 852px;
    margin: 0 auto;
    background-color: #fff;
  }

  * {
    box-sizing: border-box;
  }
`;

export default GlobalStyle;
