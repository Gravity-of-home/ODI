import React from 'react';

interface SettlementCheckModalProps {
  paidAmount: number | null;
  settleAmount: number | null;
  onClose: () => void;
  chargeFee: () => void;
}

const SettlementCheckModal: React.FC<SettlementCheckModalProps> = ({
  paidAmount,
  settleAmount,
  onClose,
  chargeFee,
}) => {
  const handleModalClick = (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation(); // 모달 내부 클릭 이벤트 처리
  };

  const handleChargeAndClose = () => {
    chargeFee();
    onClose();
  };
  const settle = settleAmount && paidAmount ? settleAmount - paidAmount : null;

  return (
    <div className='modal modal-open' onClick={onClose}>
      <div role='alert' className='alert shadow-lg ' onClick={handleModalClick}>
        <button className='btn btn-sm btn-circle absolute right-4 top-4' onClick={onClose}>
          ✕
        </button>
        <svg
          xmlns='http://www.w3.org/2000/svg'
          fill='none'
          viewBox='0 0 24 24'
          className='stroke-current shrink-0 w-6 h-6'>
          <path
            strokeLinecap='round'
            strokeLinejoin='round'
            strokeWidth='2'
            d='M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z'></path>
        </svg>
        <div>
          <h3 className='text-md'>선불 금액 : {paidAmount}</h3>
          <div className='font-bold text-xl'>추가로 내야할 금액 : {settle}</div>
        </div>
        <button onClick={handleChargeAndClose} className='btn btn-sm'>
          정산하기
        </button>
      </div>
    </div>
  );
};

export default SettlementCheckModal;
