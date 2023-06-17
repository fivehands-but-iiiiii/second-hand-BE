import { Navigate, Outlet } from 'react-router-dom';

import { getStoredValue } from '@utils/sessionStorage';

const ProtectedRoute = () => {
  const isLogin = getStoredValue({ key: 'userInfo' });

  return isLogin ? <Outlet /> : <Navigate to="/login" replace />;
};

export default ProtectedRoute;
