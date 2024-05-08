import { useNavigate } from 'react-router-dom';
import Back from '@/assets/image/icons/Back.png';

interface HeaderProps {
  isBack: boolean;
  title?: string;
  rightButtonText?: string;
  onRightButtonClick?: () => void;
}

const Header: React.FC<HeaderProps> = ({ isBack, rightButtonText, title, onRightButtonClick }) => {
  const nav = useNavigate();

  const onBack = () => {
    nav(-1);
  };
  return (
    <div className='w-full h-[5%] flex justify-between items-center px-4 bg-black'>
      {isBack && (
        <button onClick={onBack} className='w-[5%]'>
          <div className='w-[100%] h-[90%]'>
            <img src={Back} alt='뒤로가기' />
          </div>
        </button>
      )}
      <div className='flex-grow text-center text-lg font-bold text-[20px]'>{title}</div>
      {rightButtonText && onRightButtonClick && (
        <button
          onClick={onRightButtonClick}
          className='px-3 py-1 bg-OD_PURPLE rounded-lg text-white'>
          {rightButtonText}
        </button>
      )}
    </div>
  );
};

export default Header;
