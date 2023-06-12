import { createContext } from 'react';
import { BrowserRouter } from 'react-router-dom';

import GlobalStyle from '@styles/globalStyles';
import theme from '@styles/theme';

import { ThemeProvider } from 'styled-components';

import AppRouter from './routes/AppRouter';

export const UserInfoContext = createContext({});

function App() {
  return (
    <>
      <ThemeProvider theme={theme}>
        <GlobalStyle />
        <BrowserRouter>
          <AppRouter />
        </BrowserRouter>
      </ThemeProvider>
    </>
  );
}

export default App;
