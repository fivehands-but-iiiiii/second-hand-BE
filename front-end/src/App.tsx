import { RouterProvider } from 'react-router-dom';

import router from '@routes/router';
import GlobalStyle from '@styles/globalStyles';
import theme from '@styles/theme';

import { ThemeProvider } from 'styled-components';

const App = () => {
  return (
    <>
      <ThemeProvider theme={theme}>
        <GlobalStyle />
        <RouterProvider router={router} />
      </ThemeProvider>
    </>
  );
};

export default App;
