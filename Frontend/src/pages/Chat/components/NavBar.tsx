import React from 'react';
import { useParams, useNavigate } from 'react-router-dom';

interface INavBarProps {
  title: string;
}

const NavBar: React.FC<INavBarProps> = ({ title }) => {
  const { partyId } = useParams();
  const navigate = useNavigate();

  function goDetail() {
    navigate(`/chat/detail/${partyId}`);
  }

  return (
    <div className='navbar bg-base-100 justify-between'>
      <div className='flex-none'>
        <button className='btn btn-square btn-ghost'>
          <svg
            xmlns='http://www.w3.org/2000/svg'
            fill='none'
            viewBox='0 0 24 24'
            className='inline-block w-5 h-5 stroke-current'>
            <path
              strokeLinecap='round'
              strokeLinejoin='round'
              strokeWidth='2'
              d='M4 6h16M4 12h16M4 18h16'></path>
          </svg>
        </button>
      </div>
      <div className='flex'>
        <div className='btn btn-ghost text-xl'>{title}</div>
      </div>
      <div className='flex-none'>
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
    </div>
  );
};

export default NavBar;
