import React, { useState, ChangeEvent } from 'react';
import { Category } from '@/constants/constants';

const distanceOptions = [
  { value: 'distance,desc', name: '먼순서' },
  { value: 'distance,asc', name: '가까운순서' },
];

const departureOptions = [
  { value: 'departuresDate,desc', name: '먼 출발일' },
  { value: 'departuresDate,asc', name: '가까운 출발일' },
];

// 동성이면 나의 성별을 보낸다!
const genderOptions = [
  { value: 'true', name: '동성' },
  { value: 'any', name: '성별무관' },
];

const SelectFilter: React.FC = () => {
  const [selectedCategory, setSelectedCategory] = useState<string>('');
  const [selectedSort, setSelectedSort] = useState<string>('');
  const [selectedGender, setSelectedGender] = useState<string>('');
  const [isToday, setIsToday] = useState<boolean>(false);

  const handleCategoryChange = (event: ChangeEvent<HTMLSelectElement>) => {
    setSelectedCategory(event.target.value);
  };

  const handleSortChange = (event: ChangeEvent<HTMLSelectElement>) => {
    setSelectedSort(event.target.value);
  };

  const handleGenderChange = (event: ChangeEvent<HTMLSelectElement>) => {
    setSelectedGender(event.target.value);
  };

  const handleIsTodayChange = (event: ChangeEvent<HTMLInputElement>) => {
    setIsToday(event.target.checked);
  };

  return (
    <div className='flex overflow-scroll gap-2'>
      <div className='form-control'>
        <label className='label cursor-pointer flex flex-col'>
          <span className='label-text text-[13px]'>오늘출발</span>
          <input
            type='checkbox'
            className='toggle'
            checked={isToday}
            onChange={handleIsTodayChange}
          />
        </label>
      </div>

      <div className='self-center h-[100%]'>
        <label className='text-[13px]'>카테고리</label>
        <select
          className='select select-bordered select-xs'
          value={selectedCategory}
          onChange={handleCategoryChange}>
          <option value=''>전체</option>
          {Category.map(category => (
            <option key={category.tag} value={category.tag}>
              {category.name}
            </option>
          ))}
        </select>
      </div>

      <div className='self-center'>
        <label className='text-[13px]'>거리</label>
        <select
          className='select select-bordered select-xs'
          value={selectedSort}
          onChange={handleSortChange}>
          <option value=''>전체</option>
          {distanceOptions.map(option => (
            <option key={option.value} value={option.value}>
              {option.name}
            </option>
          ))}
        </select>
      </div>

      <div className='self-center'>
        <label className='text-[13px]'>시간</label>
        <select
          className='select select-bordered select-xs'
          value={selectedSort}
          onChange={handleSortChange}>
          <option value=''>전체</option>
          {departureOptions.map(option => (
            <option key={option.value} value={option.value}>
              {option.name}
            </option>
          ))}
        </select>
      </div>

      <div className='self-center'>
        <label className='text-[13px]'>동승자</label>
        <select
          className='select select-bordered select-xs'
          value={selectedGender}
          onChange={handleGenderChange}>
          <option value=''>전체</option>
          {genderOptions.map(option => (
            <option key={option.value} value={option.value}>
              {option.name}
            </option>
          ))}
        </select>
      </div>
    </div>
  );
};

export default SelectFilter;
