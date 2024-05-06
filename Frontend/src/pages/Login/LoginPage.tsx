import odi from '@/assets/image/logo/odi.png';
import NaverLogin from '@/pages/Login/components/NaverLogin';

const LoginPage = () => {
  return (
    <>
      <div className='h-screen w-full flex flex-col items-center justify-center'>
        <img src={odi}></img>
        <div className='font-["Blinker-ExtraBold"] text-[50px] pt-12'>WE'RE OD!</div>
        <div className='w-[50%] pt-24 flex flex-col'>
          <NaverLogin />
        </div>
      </div>
    </>
  );
};

export default LoginPage;
