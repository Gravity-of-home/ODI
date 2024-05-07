import { Navigate, createBrowserRouter } from 'react-router-dom';
import SplashPage from './pages/Splash/SplashPage';
import userStore from './stores/useUserStore';
import useAuth from '@/hooks/queries/useAuth';
import { useEffect } from 'react';
import loadingStore from './stores/useLoadingStore';
import NaverLoginRedirect from './pages/Login/components/NaverLoginRedirect';
import HomePage from './pages/Home/HomePage';
import ProfilePage from './pages/Profile/ProfilePage';
import LoginPage from './pages/Login/LoginPage';
import PartyDetailPage from './pages/Party/PartyDetailPage';
import ChatListPage from './pages/Chat/ChatListPage';
import ChatPage from './pages/Chat/ChatPage';
import ChatDetailPage from './pages/Chat/ChatDetailPage';

type AuthWrapperProps = {
  children: React.ReactNode;
};

const AuthWrapper: React.FC<AuthWrapperProps> = ({ children }) => {
  const { isLogin, id, loginUser, logoutUser } = userStore();
  const { refreshTokenQuery, getUserInfoQuery } = useAuth();

  useEffect(() => {
    if (refreshTokenQuery.isSuccess && getUserInfoQuery.isSuccess) {
      console.log('회원정보 조회 및 저장 완료');
      loginUser(getUserInfoQuery.userData);
    }
  }, [getUserInfoQuery.isSuccess]);

  if (!isLogin) {
    return <Navigate to='/login' replace={true} />;
  }

  return <>{children}</>;
};

const spinner = () => {
  const { isLoading } = loadingStore();

  if (isLoading) {
    return (
      <>
        <div
          className='absolute h-full w-full bg-white/50 z-20'
          onClick={e => e.stopPropagation()}
        />
        <div className='z-50 absolute left-0 right-0 top-0 bottom-0 flex justify-center items-center'>
          <svg className='animate-spin h-5 w-5 mr-3 bg-yellow-500' viewBox='0 0 24 24'></svg>
        </div>
      </>
    );
  } else {
    return <></>;
  }
};

const router = createBrowserRouter([
  {
    path: '/',
    element: <Navigate to={'/welcome'} replace={true} />,
  },
  {
    path: '/welcome',
    element: <SplashPage />,
  },
  {
    path: '/login',
    element: <LoginPage />,
  },
  {
    path: '/auth',
    element: <NaverLoginRedirect />,
  },
  {
    path: '/home',
    element: (
      <AuthWrapper>
        <HomePage />
      </AuthWrapper>
    ),
  },
  {
    path: '/profile',
    element: (
      <AuthWrapper>
        <ProfilePage />
      </AuthWrapper>
    ),
  },
  {
    path: '/party/:partyId',
    element: (
      <AuthWrapper>
        <PartyDetailPage></PartyDetailPage>
      </AuthWrapper>
    ),
  },
  {
    path: '/party/chat/:partyId',
    element: (
      <AuthWrapper>
        <ChatPage></ChatPage>
      </AuthWrapper>
    ),
  },
  {
    path: '/chatlist',
    element: (
      <AuthWrapper>
        <ChatListPage></ChatListPage>
      </AuthWrapper>
    ),
  },
  {
    path: '/chat/detail/:partyId',
    element: (
      <AuthWrapper>
        <ChatDetailPage></ChatDetailPage>
      </AuthWrapper>
    ),
  },
]);

export default router;
