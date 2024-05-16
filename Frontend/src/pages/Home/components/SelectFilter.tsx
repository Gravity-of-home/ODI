import React, { useState, useEffect, ChangeEvent } from 'react';
import userStore from '@/stores/useUserStore';
import Calendar from '@/components/Calendar';

interface ISelctFilterProps {
  setIsToday?: (isToday: boolean) => void;
  setCategory?: (category: string) => void;
  setDistance?: (distance: string) => void;
  setGender?: (gender: string) => void;
  setDeparturesDate?: (departuresDate: Date | string) => void;
}

const SelectFilter: React.FC<ISelctFilterProps> = ({
  setIsToday,
  setCategory,
  setDistance,
  setGender,
  setDeparturesDate,
}) => {
  const { gender } = userStore();
  const [selectedCategory, setSelectedCategory] = useState<string>('');
  const [selectedSort, setSelectedSort] = useState<string>('');
  const [selectedGender, setSelectedGender] = useState<string>('');
  const [isTodayChecked, setIsTodayChecked] = useState<boolean>(false);
  const today = new Date();
  const [selectedDate, setSelectedDate] = useState<number>(today.getDate());
  const [selectedMonth, setSelectedMonth] = useState<number>(today.getMonth());
  const [selectedYear, setSelectedYear] = useState<number>(today.getFullYear());

  const Category = [
    {
      tag: 'DAILY',
      name: '일상',
    },
    {
      tag: 'UNIVERSITY',
      name: '대학교',
    },
    {
      tag: 'COMMUTE',
      name: '출퇴근',
    },
    {
      tag: 'CONCERT',
      name: '콘서트',
    },
    {
      tag: 'AIRPORT',
      name: '공항',
    },
    {
      tag: 'TRAVEL',
      name: '여행',
    },
    {
      tag: 'RESERVIST',
      name: '예비군',
    },
  ];

  const distanceOptions = [
    { value: 'distance,desc', name: '먼 순서' },
    { value: 'distance,asc', name: '가까운 순서' },
  ];

  const genderOptions = [
    { value: gender, name: '동성' },
    { value: 'any', name: '성별무관' },
  ];

  const setDateClick = (day: number, month: number, year: number) => {
    setSelectedDate(day);
    setSelectedMonth(month - 1);
    setSelectedYear(year);
  };

  useEffect(() => {
    const rsvDate = `${selectedYear}-${selectedMonth + 1 >= 10 ? `${selectedMonth + 1}` : `0${selectedMonth + 1}`}-${selectedDate >= 10 ? `${selectedDate}` : `0${selectedDate}`}`;
    console.log(rsvDate);
    setDeparturesDate?.(rsvDate);
  }, [selectedDate, selectedMonth, selectedYear]);

  const handleCategoryChange = (event: ChangeEvent<HTMLSelectElement>) => {
    const category = event.target.value;
    setSelectedCategory(category);
    setCategory?.(category);
  };

  const handleSortChange = (event: ChangeEvent<HTMLSelectElement>) => {
    const sort = event.target.value;
    setSelectedSort(sort);
    setDistance?.(sort);
  };

  const handleGenderChange = (event: ChangeEvent<HTMLSelectElement>) => {
    const gender = event.target.value;
    setSelectedGender(gender);
    setGender?.(gender);
  };

  const handleIsTodayChange = (event: ChangeEvent<HTMLInputElement>) => {
    const isTodayChecked = event.target.checked;
    setIsTodayChecked(isTodayChecked);
    setIsToday?.(isTodayChecked);
    setDeparturesDate?.('');
  };

  useEffect(() => {
    // Set default value for distance
    setDistance?.('distance,asc');
  }, [setDistance]);

  return (
    <>
      <div className='flex justify-between overflow-scroll p-2 border-t-[1px] border-b-[1px] border-gray-700'>
        <div className='form-control'>
          <label className='label cursor-pointer flex flex-col'>
            <span className='label-text text-[13px] text-white'>오늘출발</span>
            <input
              type='checkbox'
              className='toggle toggle-success'
              checked={isTodayChecked}
              onChange={handleIsTodayChange}
            />
          </label>
        </div>

        <div className='self-center flex flex-col'>
          <label className='text-[13px] text-white'>카테고리</label>
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

        <div className='self-center flex flex-col'>
          <label className='text-[13px] text-white'>거리</label>
          <select
            className='select select-bordered select-xs'
            value={selectedSort}
            onChange={handleSortChange}>
            {distanceOptions.map(option => (
              <option key={option.value} value={option.value}>
                {option.name}
              </option>
            ))}
          </select>
        </div>

        <div className='self-center flex flex-col pr-1'>
          <label className='text-[13px] text-white'>동승자</label>
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
      {isTodayChecked ? null : (
        <div className='border-b-[1px] border-gray-700 animate-fadeIn'>
          <Calendar onDateClick={setDateClick} />
        </div>
      )}
    </>
  );
};

export default SelectFilter;
