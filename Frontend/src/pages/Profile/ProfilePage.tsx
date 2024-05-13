import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import { getUserInfo } from '@/apis/User';
import userStore from '@/stores/useUserStore';
import { removeCookie } from '@/utils/CookieUtil';
import SvgGoBack from '@/assets/svg/SvgGoBack';
import SvgGoInside from '@/assets/svg/SvgGoInside';
import { IUser } from '@/types/User';
import { IAPIResponse } from '@/types/APIResponse';

const ProfilePage = () => {
  useEffect(() => {
    const getUserData = async () => {
      const userInfo = await getUserInfo();
      // loginUser(userInfo);
    };

    getUserData();
  }, []);

  const {
    id,
    name,
    nickname,
    email,
    ageGroup,
    gender,
    image,
    brix,
    logoutUser,
    loginUser,
    Logout,
  } = userStore();

  const nav = useNavigate();

  const logout = () => {
    logoutUser();
    Logout();
    removeCookie('Authorization');
    nav('/login', { replace: true });
  };

  return (
    <>
      <div className='w-[100%] h-[100%] bg-gray-800'>
        <div className='relative w-[100%] h-[5%] bg-black z-10 flex items-center'>
          <div
            className='px-4 z-10'
            onClick={() => {
              nav('/home', { replace: true });
            }}>
            <SvgGoBack />
          </div>
          <div className='absolute w-[100%] flex justify-center text-[18px] font-semibold text-white'>
            MY PAGE
          </div>
          {/* <div
            className='absolute w-[15%] h-[80%] right-0 mx-4 bg-OD_PURPLE rounded-lg flex justify-center items-center font-semibold text-white hover:text-OD_YELLOW'
            onClick={() => {}}>
            수정
          </div> */}
        </div>

        <div className='w-[100%] h-[95%] flex flex-col items-center'>
          <div className='w-[100%] h-[20%] flex justify-between items-center px-5 mt-5'>
            <div className='w-[30%] flex justify-center items-center rounded-full border border-slate-500 overflow-hidden'>
              <img src={image} alt='프로필 사진' />
            </div>
            <div className='w-[55%] h-[100%] flex flex-col justify-center'>
              <div className='h-[15%] flex items-center text-gray-400'>닉네임</div>
              <div className='h-[30%] flex items-center text-white text-[20px]'>{nickname}</div>
              <div className='h-[15%] flex items-center text-gray-400'>이름</div>
              <div className='h-[30%] flex items-center text-white text-[20px]'>{name}</div>
            </div>
            <div
              className='w-[5%] h-[100%] flex justify-center items-center'
              onClick={() => {
                /* 자세한 프로필 내역 및 수정하기! */
              }}>
              <SvgGoInside style={{ width: '50%', height: '30%' }} />
            </div>
          </div>
          {/* <div>이메일 : {email}</div>
          <div>나이 : {ageGroup}</div>
          <div>성별 : {gender === 'M' ? '남자' : '여자'}</div>
          <div>당도 : {brix}</div> */}
          <div className='w-[90%] border-b-2 border-gray-500 mx-5'></div>
          <div>
            <div>내 포인트</div>
          </div>
          <button
            className='btn btn-outline rounded-full py-3 px-8 font-bold hover:bg-[#A93BFF]'
            onClick={logout}>
            로그아웃
          </button>
        </div>
      </div>
    </>
  );
};

export default ProfilePage;
