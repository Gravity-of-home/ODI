import { useNavigate } from 'react-router-dom';
import userStore from '@/stores/useUserStore';
import { removeCookie } from '@/utils/CookieUtil';

const ProfilePage = () => {
  const nav = useNavigate();

  const { logoutUser, Logout } = userStore();

  const logout = () => {
    logoutUser();
    Logout();
    removeCookie('Authorization');
    nav('/login', { replace: true });
  };

  return (
    <>
      <div className='flex flex-col justify-center items-center w-full h-screen'>
        <button
          className='btn btn-outline rounded-full py-3 px-8 font-bold hover:bg-[#A93BFF]'
          onClick={logout}>
          로그아웃
        </button>
      </div>
    </>
  );
};

export default ProfilePage;
