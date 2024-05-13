import { useNavigate } from 'react-router-dom';
import userStore from '@/stores/useUserStore';
import { removeCookie } from '@/utils/CookieUtil';
import SvgGoBack from '@/assets/svg/SvgGoBack';

const ProfilePage = () => {
  const nav = useNavigate();

  const { id, name, nickname, email, ageGroup, gender, image, brix, logoutUser, Logout } =
    userStore();

  const logout = () => {
    logoutUser();
    Logout();
    removeCookie('Authorization');
    nav('/login', { replace: true });
  };

  return (
    <>
      <div className='fixed w-[100%] h-[5%] bg-black z-10 flex items-center'>
        <div
          className='px-4 z-10'
          onClick={() => {
            nav('/home', { replace: true });
          }}>
          <SvgGoBack />
        </div>
        <div className='fixed w-[100%] flex justify-center text-[18px] font-semibold text-white'>
          MY PAGE
        </div>
      </div>
      <div className='flex flex-col justify-center items-center w-[100%] h-[100%]'>
        <div className='font-["Pretendard-Bold"] text-[30px]'>userStore 테스트</div>
        <div className='w-[80%] h-[70%] flex flex-col items-center'>
          <div>
            <img src={image} alt='프로필 사진' />
          </div>
          <div>ID : {id}</div>
          <div>이름 : {name}</div>
          <div>닉네임 : {nickname}</div>
          <div>이메일 : {email}</div>
          <div>나이 : {ageGroup}</div>
          <div>성별 : {gender === 'M' ? '남자' : '여자'}</div>
          <div>당도 : {brix}</div>
        </div>
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
