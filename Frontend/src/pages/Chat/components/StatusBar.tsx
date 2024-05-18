import React from 'react';

interface IStatusBarProps {
  currentStep: number;
  totalSteps: number;
}

const StatusBar: React.FC<IStatusBarProps> = ({ currentStep, totalSteps }) => {
  const progress = currentStep + 1 > totalSteps ? 100 : ((currentStep + 1) / totalSteps) * 100;
  return (
    <div className='w-[100%] h-4 bg-gray-200 rounded-full mt-3'>
      <div
        className='w-[100%] h-[100%] bg-green-500 rounded-full transition-all duration-500 flex justify-center items-center text-white font-bold text-[13px]'
        style={{ width: `${progress}%` }}>
        {progress.toFixed(0)}%
      </div>
    </div>
  );
};

export default StatusBar;
