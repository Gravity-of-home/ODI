import { ViteConfig } from '@/apis/ViteConfig';
import NAVER_DARK from '@/assets/image/logo/naver_dark.png';

const NaverLogin = () => {
  const BASE_URI = ViteConfig.VITE_BASE_URL;
  const link = `${BASE_URI}/oauth2/authorization/naver`;

  const loginHandler = () => {
    window.location.href = link;
  };

  return (
    <>
      <button
        className='flex items-center gap-3 border border-[#E4E4E4] p-2 rounded-xl'
        onClick={loginHandler}>
        <div className='w-[10%]'>
          <img src={NAVER_DARK}></img>
        </div>
        <div className='font-bold'>네이버로 시작하기</div>
      </button>
    </>
  );
};

export default NaverLogin;
