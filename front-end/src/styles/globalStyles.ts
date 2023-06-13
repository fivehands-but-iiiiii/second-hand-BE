import { createGlobalStyle } from 'styled-components';
import { normalize } from 'styled-normalize';

const GlobalStyle = createGlobalStyle`
  ${normalize}

  html,
  body {
    overflow: hidden;
    background-color: tomato;
  }

  * {
    box-sizing: border-box;
  }

  button {
    border: none;
    cursor: pointer;
    background-color: transparent;
    line-height: normal;
  }

  a {
    text-decoration: none;
  }
`;

export default GlobalStyle;
