// import getUserInfoQuery
import { useEffect } from 'react';
import Map from '@/components/Maps/Map';

const HomePage = () => {
  useEffect(() => {
    // const userInfo = getUserInfo();
    // console.log('USER INFO: ', userInfo);
  }, []);
  return (
    <>
      <h1>여기는 홈 화면</h1>
      <p>실시간 예약 버튼이랑 실시간 예약, 인기 예약 보여줄 예정</p>
      <Map />
    </>
  );
};

export default HomePage;
