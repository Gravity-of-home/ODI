import React from 'react';
import { useNavigate } from 'react-router-dom';

interface SettlementFailModalProps {
  onClose: () => void; // onClose prop의 타입 지정
}

const SettlementFailModal: React.FC<SettlementFailModalProps> = ({ onClose }) => {
  const handleModalClick = (e: React.MouseEvent<HTMLDivElement>) => {
    e.stopPropagation(); // 모달 내부 클릭 이벤트 처리
  };
  const navigate = useNavigate();
  function goRecharge() {
    navigate('/charge');
  }
  return (
    <div className='modal modal-open' onClick={onClose}>
      <div className='modal-box relative' onClick={handleModalClick}>
        <div role='alert' className='alert shadow-lg'>
          <button className='btn btn-sm btn-circle absolute right-4 top-4' onClick={onClose}>
            ✕
          </button>
          <svg
            xmlns='http://www.w3.org/2000/svg'
            className='stroke-current shrink-0 h-6 w-6'
            fill='none'
            viewBox='0 0 24 24'>
            <path
              strokeLinecap='round'
              strokeLinejoin='round'
              strokeWidth='2'
              d='M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z'
            />
          </svg>
          <div>
            <h3 className='font-bold'>정산오류!</h3>
            <div className='text-xs'>"포인트 부족으로 정산에 실패했습니다."</div>
          </div>
          <button onClick={goRecharge} className='btn btn-sm'>
            포인트 충전하러가기
          </button>
        </div>
      </div>
    </div>
  );
};

export default SettlementFailModal;
