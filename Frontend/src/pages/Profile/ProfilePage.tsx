import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import { getUserInfo } from '@/apis/User';
import userStore from '@/stores/useUserStore';
import { removeCookie } from '@/utils/CookieUtil';
import SvgGoBack from '@/assets/svg/SvgGoBack';
import SvgGoInside from '@/assets/svg/SvgGoInside';
import { IUser } from '@/types/User';
import { IAPIResponse } from '@/types/APIResponse';
import ProgressBar from '@/components/ProgressBar';

const ProfilePage = () => {
  useEffect(() => {
    const getUserData = async () => {
      const { data } = await getUserInfo();
      loginUser(data);
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
    point,
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
        </div>
        <div className='w-[100%] h-[95%] flex flex-col items-center'>
          <div className='w-[100%] h-[20%] flex justify-between items-center px-5 mt-5'>
            <div className='w-[150px] h-[150px] flex justify-center items-center rounded-full border border-slate-500 overflow-hidden'>
              <img src={image} alt='프로필 사진' />
            </div>
            <div className='w-[40%] h-[100%] flex flex-col justify-center'>
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
              <SvgGoInside style={{ width: '24px', height: '24px' }} />
            </div>
          </div>
          <div className='w-[90%] border-b-2 border-gray-500 mx-5'></div>
          <div className='w-[100%] h-[10%] flex flex-col justify-evenly my-3 px-5'>
            <div className='font-semibold text-white text-[20px]'>나의 당도</div>
            <div className='font-semibold text-white text-[18px] flex justify-end'>
              <ProgressBar targetValue={brix as number} />
            </div>
          </div>
          <div className='w-[90%] border-b-2 border-gray-500 mx-5'></div>
          <div className='w-[100%] h-[15%] flex flex-col justify-evenly my-3 px-5'>
            <div className='font-semibold text-white text-[20px]'>내 포인트</div>
            <div className='font-semibold text-white text-[18px] flex justify-end'>
              {point}
              <span className='text-slate-500 px-2'>오디</span>
            </div>
            <button
              className='w-[60%] h-[30%] flex justify-center items-center bg-OD_YELLOW self-end rounded-xl text-black font-semibold text-[18px]'
              onClick={() => {
                /* 충전하기 페이지 */
              }}>
              충전하기
            </button>
          </div>
          <div className='w-[90%] border-b-2 border-gray-500 mx-5'></div>
          <div className='w-[100%] h-[10%] flex flex-col justify-evenly my-3 px-5'>
            <div className='font-semibold text-white text-[20px]'>이용 내역</div>
            <div className='font-semibold text-white flex justify-end'>
              <SvgGoInside style={{ width: '24px', height: '24px' }} />
            </div>
          </div>
          <div className='w-[90%] border-b-2 border-gray-500 mx-5'></div>
          <div className='w-[100%] h-[10%] flex flex-col justify-evenly my-3 px-5'>
            <div className='font-semibold text-white text-[20px]'>결제 내역</div>
            <div className='font-semibold text-white flex justify-end'>
              <SvgGoInside style={{ width: '24px', height: '24px' }} />
            </div>
          </div>
          <div className='w-[90%] border-b-2 border-gray-500 mx-5'></div>
          <button
            className='btn btn-outline w-[80%] rounded-full mt-12 py-3 px-8 font-bold text-[20px] text-white bg-slate-500 hover:bg-[#A93BFF] hover:text-white'
            onClick={logout}>
            로그아웃
          </button>
        </div>
      </div>
    </>
  );
};

export default ProfilePage;
