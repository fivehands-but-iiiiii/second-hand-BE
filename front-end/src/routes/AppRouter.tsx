import { Route, Routes, Navigate, useLocation } from 'react-router-dom';

import Home from '@pages/Home';
// import Join from '@pages/Join';
import MobileLayout from '@pages/Layout';
import Login from '@pages/Login';

const AppRouter = () => {
  // TODO: 로그인 승인 후 로직 변경예정
  // const isAuthorized = window.localStorage.getItem('isAuthorized');
  // const location = useLocation();

  return (
    <Routes>
      <Route element={<MobileLayout />}>
        {/* {!isAuthorized ? (
        <Navigate
          to="/login"
          replace
          state={{ redirectedFrom: location.pathname }}
        />
      ) : (
        <Navigate
          to="/"
          replace
          state={{ redirectedFrom: location.pathname }}
        />
      )} */}
        <Route path="/login" element={<Login />} />
        {/* <Route path="/join" element={<Join />} /> */}
        <Route path="/" element={<Home />} />
      </Route>
    </Routes>
  );
};
export default AppRouter;
