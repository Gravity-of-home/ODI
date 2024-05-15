import React, { useState, useEffect } from 'react';

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
  hostName: string;
  hostImgUrl: string;
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
  hostName,
  hostImgUrl,
}) => {
  const categoryDescriptions: Record<string, string> = {
    UNIVERSITY: '대학교',
    DAILY: '일상',
    COMMUTE: '출퇴근',
    CONCERT: '콘서트',
    AIRPORT: '공항',
    TRAVEL: '여행',
    RESERVIST: '예비군',
  };

  return (
    <div className='container p-4'>
      <div className='flex justify-between'>
        <div className='flex gap-x-2'>
          <div className='badge py-3 text-center content-center rounded bg-yellow-100'>
            <p className=''>{categoryDescriptions[category]}</p>
          </div>
          <div className='badge py-3 text-center content-center rounded bg-slate-100'>
            <p className=''>{genderRestriction === 'ANY' ? '성별무관' : '동성만'}</p>
          </div>
        </div>
        <div
          className={`py-3 badge flex justify-center rounded ${state === 'GATHERING' ? 'bg-blue-100 text-blue-500' : 'bg-red-100 text-red-500'} font-bold`}>
          <div className='flex text-center '>
            <p>{state === 'GATHERING' ? '모집중' : '모집마감'}</p>
            {state === 'GATHERING' && (
              <p className='ml-1'>
                {currentParticipants}/{maxParticipants}
              </p>
            )}
          </div>
        </div>
      </div>
      <div className='mt-4'>
        <p>
          일정 <span className='font-bold'>{departuresDate} 출발</span>
        </p>
        <p>
          예상 시간 <span className='font-bold'>{expectedTime}분 소요</span>
        </p>
        <p>
          예상 비용
          <span className='font-bold'>
            {' '}
            총 {Math.round(expectedCost).toLocaleString()}원 · 1인당{' '}
            {(expectedCost / maxParticipants).toLocaleString()}원({maxParticipants}인 기준)
          </span>
        </p>
      </div>
      <div className='divider'></div>

      <div className='content'>
        <div className='chat chat-start'>
          <div className='chat-image'>
            <img className='h-10 w-10 rounded-full ' alt='host' src={hostImgUrl} />
          </div>
          <div className='chat-header'>{hostName}</div>
          <div className='chat-bubble chat-bubble-primary text-white'>{content}</div>
        </div>
      </div>
    </div>
  );
};

export default PartyInfo;
