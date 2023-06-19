import { createBrowserRouter } from 'react-router-dom';

import Join from '@components/login/Join';
import OAuthCallback from '@components/login/OAuthCallback';
import ErrorPage from '@pages/ErrorPage';
import Home from '@pages/Home';
import MobileLayout from '@pages/Layout';
import Login from '@pages/Login';

const router = createBrowserRouter([
  {
    path: '/',
    element: <MobileLayout />,
    errorElement: <ErrorPage />,
    children: [
      {
        path: '',
        element: <Home />,
      },
      {
        path: 'login',
        element: <Login />,
      },
    ],
  },
  {
    path: 'login/oauth2/code/github',
    element: <OAuthCallback />,
  },
  {
    path: 'join',
    element: <Join />,
  },
]);

export default router;
