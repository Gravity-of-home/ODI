import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';

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
        <div className='pb-10 text-gray-500'>우리들의 택시 합승 서비스</div>
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
