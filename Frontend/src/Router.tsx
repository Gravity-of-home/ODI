import { Navigate, createBrowserRouter } from 'react-router-dom';
import userStore from './stores/useUserStore';
import loadingStore from './stores/useLoadingStore';
import HomePage from './pages/Home/HomePage';
import SplashPage from './pages/Splash/SplashPage';
import NaverLogin from './pages/Login/components/NaverLogin';
import NaverLoginRedirect from './pages/Login/components/NaverLoginRedirect';

type AuthWrapperProps = {
  children: React.ReactNode;
};

const AuthWrapper: React.FC<AuthWrapperProps> = ({ children }) => {
  const isLogin = userStore(state => state.isLogin);

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
    element: <NaverLogin />,
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
]);

export default router;
