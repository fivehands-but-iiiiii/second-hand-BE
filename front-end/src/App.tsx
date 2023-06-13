import { BrowserRouter } from 'react-router-dom';

import Tabs from '@components/common/TabBar/Tabs';
import GlobalStyle from '@styles/globalStyles';
import theme from '@styles/theme';

import { ThemeProvider } from 'styled-components';

import AppRouter from './routes/AppRouter';

const App = () => {
  return (
    <>
      <ThemeProvider theme={theme}>
        <GlobalStyle />
        <BrowserRouter>
          <AppRouter />
          <Tabs></Tabs>
        </BrowserRouter>
      </ThemeProvider>
    </>
  );
};

export default App;
