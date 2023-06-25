import { createBrowserRouter } from 'react-router-dom';

import Join from '@components/login/Join';
import OAuthCallback from '@components/login/OAuthCallback';
import ChatList from '@pages/ChatList';
import ErrorPage from '@pages/ErrorPage';
import Home from '@pages/Home';
import MobileLayout from '@pages/Layout';
import Login from '@pages/Login';
import New from '@pages/New';
import SalesHistory from '@pages/SalesHistory';
import WishList from '@pages/WishList';

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
      {
        path: 'wishList',
        element: <WishList />,
      },
      {
        path: 'items/:userId',
        element: <SalesHistory />,
      },
      {
        path: 'chatList/:userId',
        element: <ChatList />,
      },
      {
        path: 'new',
        element: <New />,
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
