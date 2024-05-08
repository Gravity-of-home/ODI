import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';

interface INavBarProps {
  title: string;
  departuresName: string;
  arrivalsName: string;
  departuresDate: string;
  state: string;
}

const NavBar: React.FC<INavBarProps> = ({
  title,
  departuresName,
  arrivalsName,
  departuresDate,
  state,
}) => {
  const { partyId } = useParams();
  const navigate = useNavigate();
  let stateComponent;
  if (state === '') {
    stateComponent = <div className='badge badge-outline'>{state}</div>;
  } else if (state === '') {
    stateComponent = <div className='badge badge-primary badge-outline'>{state}</div>;
  } else if (state === 'COMPLETED') {
    stateComponent = <div className='badge badge-secondary badge-outline'>모집마감</div>;
  }
  function goDetail() {
    navigate(`/chat/detail/${partyId}`);
  }
  function goBack() {
    navigate(`/party/${partyId}`);
  }

  return (
    <div className='fixed top-0 bg-white w-screen z-10'>
      <div className='flex items-center justify-between'>
        <button onClick={goBack} className='btn btn-ghost btn-circle text-3xl'>
          {'<'}
        </button>
        <p className='font-bold text-2xl flex-grow text-center'>{title}</p>
        <button onClick={goDetail} className='btn btn-square btn-ghost'>
          <svg
            xmlns='http://www.w3.org/2000/svg'
            fill='none'
            viewBox='0 0 24 24'
            className='inline-block w-5 h-5 stroke-current'>
            <path
              strokeLinecap='round'
              strokeLinejoin='round'
              strokeWidth='2'
              d='M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z'></path>
          </svg>
        </button>
      </div>
      <div className='flex'>
        <div className='px-4'>{stateComponent}</div>
        <div>
          <p>
            {departuresName} {'>'} {arrivalsName}
          </p>
          <p>{departuresDate}</p>
        </div>
      </div>
      <div className='button'>
        <p>1/N 정산하기</p>
      </div>
    </div>
  );
};

export default NavBar;
