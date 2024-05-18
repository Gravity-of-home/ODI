import odi from '@/assets/image/logo/odi.png';
import NaverLogin from '@/pages/Login/components/NaverLogin';

const LoginPage = () => {
  return (
    <>
      <div className='h-screen w-full flex flex-col items-center justify-center'>
        <img src={odi}></img>
        <div className='font-["Blinker-ExtraBold"] text-[50px] pt-12'>WE'RE OD!</div>
        <div className='pt-5 text-gray-500'>사람들과 함께하는</div>
        <div className='pt-2 pb-10 text-gray-500'>택시 합승 서비스</div>
        <div className='w-[50%] pt-18 flex flex-col'>
          <NaverLogin />
        </div>
      </div>
    </>
  );
};

export default LoginPage;
