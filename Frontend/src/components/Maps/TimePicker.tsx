import React, { useState } from 'react';

interface TimePickerProps {
  onTimeChange?: (hour: number, minute: number) => void;
}

const TimePicker: React.FC<TimePickerProps> = ({ onTimeChange }) => {
  const now = new Date();
  const [selectedHour, setSelectedHour] = useState(now.getHours());
  const [selectedMinute, setSelectedMinute] = useState(now.getMinutes());

  const handleHourChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const newHour = parseInt(event.target.value);
    setSelectedHour(newHour);
    onTimeChange?.(newHour, selectedMinute);
  };

  const handleMinuteChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    const newMinute = parseInt(event.target.value);
    setSelectedMinute(newMinute);
    onTimeChange?.(selectedHour, newMinute);
  };

  return (
    <div className='flex flex-row items-center text-white px-4 py-4'>
      <select
        value={selectedHour}
        onChange={handleHourChange}
        className='select select-ghost w-30 text-[15px] focus:outline-none'>
        {[...Array(24).keys()].map(hour => (
          <option key={hour} value={hour} className='text-center'>
            {`${hour.toString().padStart(2, '0')} 시`}
          </option>
        ))}
      </select>
      <div className='w-[5%] text-center'>:</div>
      <select
        value={selectedMinute}
        onChange={handleMinuteChange}
        className='select select-ghost w-30 text-[15px] focus:outline-none'>
        {[...Array(60).keys()].map(minute => (
          <option key={minute} value={minute} className='text-center'>
            {`${minute.toString().padStart(2, '0')} 분`}
          </option>
        ))}
      </select>
    </div>
  );
};

export default TimePicker;
