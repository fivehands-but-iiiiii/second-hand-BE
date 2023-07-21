import { createBrowserRouter } from 'react-router-dom';

import CategoryProvider from '@components/context/CategoryContext';
import MobileLayout from '@components/layout/MobileLayout';
import Join from '@components/login/Join';
import OAuthCallback from '@components/login/OAuthCallback';
import ChatPage from '@pages/ChatPage';
import ErrorPage from '@pages/ErrorPage';
import Home from '@pages/Home';
import Login from '@pages/Login';
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
        element: (
          <CategoryProvider>
            <Home />
          </CategoryProvider>
        ),
      },
      {
        path: 'login',
        element: <Login />,
      },
      {
        path: 'wish-list',
        element: (
          <CategoryProvider>
            <WishList />
          </CategoryProvider>
        ),
      },
      {
        path: 'sales-history',
        element: (
          <CategoryProvider>
            <SalesHistory />
          </CategoryProvider>
        ),
      },
      {
        path: 'chat-list',
        element: <ChatPage />,
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
