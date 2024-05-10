import { useNavigate } from 'react-router-dom';
import SvgGoBack from '@/assets/svg/SvgGoBack';

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
    <div className='w-[100%] h-[5%] flex justify-between items-center bg-black'>
      {isBack && (
        <div
          className='px-4 z-10'
          onClick={() => {
            nav(-1);
          }}>
          <SvgGoBack />
        </div>
      )}
      <div className='fixed w-[100%] h-[5%] flex justify-center items-center font-semibold text-[18px] text-white'>
        {title}
      </div>
      {rightButtonText && onRightButtonClick && (
        <button
          onClick={onRightButtonClick}
          className='mx-4 px-3 py-1 bg-OD_PURPLE rounded-lg text-white z-10'>
          {rightButtonText}
        </button>
      )}
    </div>
  );
};

export default Header;
