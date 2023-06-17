import { Navigate, Outlet } from 'react-router-dom';

const ProtectedRoute = () => {
  const isLogin = sessionStorage.getItem('userInfo');

  return isLogin ? <Outlet /> : <Navigate to="/login" replace />;
};

export default ProtectedRoute;
