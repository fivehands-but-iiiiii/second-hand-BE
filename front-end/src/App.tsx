import { useState, createContext } from 'react';
import Login, { UserInfo } from './pages/Login';
import GlobalStyle from './styles/globalStyles';
export const UserInfoContext = createContext({});

function App() {
  const [userInfo, setUserInfo] = useState({});
  const handleUserInfo = (userInfo: UserInfo) => {
    setUserInfo(userInfo);
  };

  return (
    <>
      <UserInfoContext.Provider value={{ userInfo, handleUserInfo }}>
        <GlobalStyle />
        <div className="app">
          <Login handleUserInfo={handleUserInfo} />
        </div>
      </UserInfoContext.Provider>
    </>
  );
}

export default App;
