import { useNavigate } from 'react-router-dom';
import userStore from '@/stores/useUserStore';
import { removeCookie } from '@/utils/CookieUtil';

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
      <div className='flex flex-col justify-center items-center w-full h-screen'>
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
