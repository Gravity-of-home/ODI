import { useEffect } from 'react';

export default function NaverLoginRedirect() {
  useEffect(() => {
    // TODO : Access Token 쿠키 -> 전역관리 OR 로컬 스토리지 저장
    // TODO : utils 폴더 생성 -> 로그인 판별 및 토큰 만료 갱신 & CRUD reissue OR 페이지 이동 시 reissue 진행
    // TODO : 로그인 성공시 HomePage 이동
  });

  return (
    <div className='w-full h-screen flex flex-col justify-center items-center relative'>
      <div>로그인 진행중입니다...</div>
    </div>
  );
}
