import { useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';

const PaymentsFail = () => {
  const failNotificationModalRef = useRef<HTMLDialogElement>(null);
  const nav = useNavigate();

  const openModal = () => {
    if (failNotificationModalRef.current) {
      failNotificationModalRef.current.showModal();
    }
  };

  useEffect(() => {
    openModal();
  }, []);

  return (
    <>
      <div className='w-[100%] h-[100%] bg-white'>
        <dialog ref={failNotificationModalRef} id='my_modal_4' className='modal'>
          <div className='modal-box w-11/12'>
            <h3 className='font-bold text-black text-[20px]'>결제실패</h3>
            <div className='mt-1 border border-gray-500'></div>
            <div className='flex flex-col justify-center items-center'>
              <div className='flex justify-center items-center w-[150px] h-[150px] my-5'>
                <img
                  src='https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Smilies/Crying%20Face.png'
                  alt='Crying Face'
                  width='100%'
                  height='100%'
                />
              </div>

              <div className='text-gray-500 font-semibold text-[20px] my-2'>결제에 실패했어요!</div>
              <div className='text-gray-500 font-semibold text-[16px]'>다시 시도해 주세요!</div>
            </div>
            <div className='modal-action'>
              <form method='dialog'>
                <button
                  className='btn btn-sm btn-circle btn-ghost absolute right-5 top-5'
                  onClick={() => nav('/profile/payments')}>
                  ✕
                </button>
                <button
                  className='btn bg-OD_PURPLE text-white'
                  onClick={() => nav('/profile/payments')}>
                  확인
                </button>
              </form>
            </div>
          </div>
        </dialog>
      </div>
    </>
  );
};

export default PaymentsFail;
