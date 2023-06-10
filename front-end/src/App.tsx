import Login, { UserInfo } from '@pages/Login';
import { useState, createContext } from 'react';
import { ThemeProvider } from 'styled-components';
import GlobalStyle from './styles/globalStyles';
import theme from './styles/theme';
export const UserInfoContext = createContext({});

function App() {
  const [userInfo, setUserInfo] = useState({});
  const handleUserInfo = (userInfo: UserInfo) => {
    setUserInfo(userInfo);
  };

  return (
    <>
      <ThemeProvider theme={theme}>
        <UserInfoContext.Provider value={{ userInfo, handleUserInfo }}>
          <GlobalStyle />
          <div className="app">
            <Login handleUserInfo={handleUserInfo} />
          </div>
        </UserInfoContext.Provider>
      </ThemeProvider>
    </>
  );
}

export default App;
