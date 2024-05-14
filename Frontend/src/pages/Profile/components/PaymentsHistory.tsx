import { useNavigate } from 'react-router-dom';
import { useEffect } from 'react';
import SvgGoBack from '@/assets/svg/SvgGoBack';

const PaymentsHistory = () => {
  const nav = useNavigate();
  useEffect(() => {}, []);

  return (
    <>
      <div className='w-[100%] h-[100%] bg-gray-800'>
        <div className='relative w-[100%] h-[5%] bg-black z-10 flex items-center'>
          <div
            className='px-4 z-10'
            onClick={() => {
              nav(-1);
            }}>
            <SvgGoBack />
          </div>
          <div className='absolute w-[100%] flex justify-center text-[18px] font-semibold text-white'>
            결제 내역
          </div>
        </div>
      </div>
    </>
  );
};

export default PaymentsHistory;
