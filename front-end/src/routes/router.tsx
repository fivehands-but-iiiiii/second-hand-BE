import { createBrowserRouter } from 'react-router-dom';

// import ChatList from '@components/chat/ChatList';
import MobileLayout from '@components/layout/MobileLayout';
import Join from '@components/login/Join';
import OAuthCallback from '@components/login/OAuthCallback';
import ChatRooms from '@pages/ChatRooms';
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
        path: 'chatList',
        element: <ChatRooms />,
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
