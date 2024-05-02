import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import { refreshJWT } from '@/utils/JWTUtil.ts';

const SplashPage = () => {
  const nav = useNavigate();

  const goMain = () => {
    nav('/home');
  };

  useEffect(() => {
    const timeout = setTimeout(() => {
      goMain();
    }, 3000);

    return () => clearTimeout(timeout);
  }, []);

  return (
    <>
      <div className='flex flex-col justify-center items-center w-full h-screen'>
        <div className='font-["Blinker-ExtraBold"] text-[85px]'>O D !</div>
        <div className='pb-10 text-[20px]'>Our Destination!</div>
        {/* TODO : 시작하기는 TEST로 넣어두었습니다! */}
        <button
          className='btn btn-outline rounded-full py-3 px-8 font-bold hover:bg-[#A93BFF]'
          onClick={goMain}>
          시작하기
        </button>
      </div>
    </>
  );
};
export default SplashPage;
