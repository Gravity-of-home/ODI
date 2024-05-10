import { useNavigate } from 'react-router-dom';

const NotFoundPage = () => {
  const nav = useNavigate();

  const onClickHandler = () => {
    nav('/home');
  };

  return (
    <div
      className='gird grid-cols-1 gap-6 h-[100%] flex flex-col justify-center items-center animate-fadeIn'
      onClick={onClickHandler}>
      <img
        src='https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Smilies/Face%20with%20Crossed-Out%20Eyes.png'
        alt='Face with Crossed-Out Eyes'
        width='200'
        height='200'
      />
      <div className="font-['Pretendard-Bold'] text-[36px]">잘못된 경로입니다!</div>
      <div className='text-subText text-center text-[20px]'>404 Not Found</div>
    </div>
  );
};

export default NotFoundPage;
