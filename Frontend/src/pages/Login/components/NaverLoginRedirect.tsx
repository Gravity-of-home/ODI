import { useEffect } from 'react';
import { getCookie, removeCookie } from '@/utils/CookieUtil.ts';
import userStore from '@/stores/useUserStore';
import { useNavigate } from 'react-router-dom';

const NaverLoginRedirect = () => {
  const { Login } = userStore();
  const nav = useNavigate();

  useEffect(() => {
    Login();
    // TODO : 로그인 후 USER-ID 필요! -> 로그인한 유저의 사용자 정보를 전역관리!

    nav('/home', { replace: true });
  }, []);

  removeCookie('Authorizatio');

  return (
    <div className='w-full h-screen flex flex-col justify-center items-center relative'>
      <div>로그인 진행중입니다...</div>
    </div>
  );
};

export default NaverLoginRedirect;
