import React, { useState, useEffect } from 'react';
import axios from 'axios'; // axios 설치 필요

interface IPartyProps {
  category: string;
  state: string;
  currentParticipants: number;
  maxParticipants: number;
  departuresDate: string;
  expectedTime: number;
  expectedCost: number;
  content: string;
  genderRestriction: string;
}

const PartyInfo: React.FC<IPartyProps> = ({
  category,
  state,
  currentParticipants,
  maxParticipants,
  departuresDate,
  expectedTime,
  expectedCost,
  content,
  genderRestriction,
}) => {
  return (
    <div className='container p-4'>
      <div className='flex justify-between'>
        <div className='flex gap-x-2'>
          <div className='text-center content-center w-12 rounded bg-yellow-100'>{category}</div>
          <div className='text-center content-center w-20 rounded bg-slate-100'>
            <p className='p-2'>{genderRestriction === 'ANY' ? '성별무관' : '동성만'}</p>
          </div>
        </div>
        <div
          className={`w-32 flex justify-center rounded ${state === 'GATHERING' ? 'bg-blue-100 text-blue-500' : 'bg-red-100 text-red-500'} font-bold`}>
          <div className='flex p-2 text-center'>
            <p>{state === 'GATHERING' ? '모집중' : '모집마감'}</p>
            <p>
              {currentParticipants}/{maxParticipants}
            </p>
          </div>
        </div>
      </div>
      <div className='mt-4'>
        <p>
          일정 <span className='font-bold'>{departuresDate} 출발</span>
        </p>
        <p>
          예상시간 <span className='font-bold'>{expectedTime}분 소요</span>
        </p>
        <p>
          예상비용
          <span className='font-bold'>
            {' '}
            총 {expectedCost.toLocaleString()} · 1인당{' '}
            {(expectedCost / maxParticipants).toLocaleString()}원({maxParticipants}인)
          </span>
        </p>
      </div>
      <div className='mt-4'>
        <p>{content}</p>
      </div>
    </div>
  );
};

export default PartyInfo;
