import React, { useState, useEffect } from 'react';

interface ProgressBarProps {
  targetValue: number;
}

const ProgressBar: React.FC<ProgressBarProps> = ({ targetValue }) => {
  const [progress, setProgress] = useState(0);

  useEffect(() => {
    // 값을 점진적으로 증가시키기 위해 setInterval 사용
    const timer = setInterval(() => {
      setProgress(prevProgress => {
        const nextProgress = prevProgress + 1;
        if (nextProgress > targetValue) {
          clearInterval(timer);
          return targetValue; // 목표치에 도달하면 종료
        }
        return nextProgress;
      });
    }, 50); // 30ms 마다 실행하여 부드러운 애니메이션 효과 구현

    return () => clearInterval(timer);
  }, [targetValue]);

  return (
    <div className='w-[100%] bg-gray-400 h-4 rounded-full'>
      <div
        className='bg-OD_PURPLE h-4 rounded-full transition-width duration-500 flex justify-center items-center text-white font-bold text-sm'
        style={{ width: `${progress}%` }}>
        {progress.toFixed(1)}
      </div>
    </div>
  );
};

export default ProgressBar;
