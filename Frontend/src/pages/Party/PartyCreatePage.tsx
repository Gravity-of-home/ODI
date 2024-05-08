import TopHeader from '@/components/TopHeader.tsx';
import SvgDepartureMarker from '@/assets/svg/SvgDepartureMarker';
import SvgArrivalMarker from '@/assets/svg/SvgArrivalMarker';
import Front from '@/assets/image/icons/Front.png';
import { useState, useRef, ChangeEvent } from 'react';
import partyStore from '@/stores/usePartyStore';
import PartyMap from './components/PartyMap';

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

const PartyCreatePage = () => {
  const { departuresName, departuresLocation, arrivalsName, arrivalsLocation } = partyStore();
  const [title, setTitle] = useState<string>('');
  const titleRef = useRef<HTMLInputElement>(null);
  const [departuresDate, setDeparturesDate] = useState<Date | string>(new Date());
  const [maxParticipants, setMaxParticipants] = useState<number>(4);
  const [genderRestriction, setGenderRestriction] = useState<boolean>(false);
  const [content, setContent] = useState<string>('');
  const contentRef = useRef<HTMLTextAreaElement>(null);

  const changeTitle = (e: ChangeEvent<HTMLInputElement>) => {
    setTitle(e.target.value);
    console.log(title);
  };
  const changeContent = (e: ChangeEvent<HTMLTextAreaElement>) => {
    if (e.target.value.length > 100) return;
    setContent(e.target.value);
  };

  return (
    <div className='w-[100%] h-[95%]'>
      <TopHeader
        isBack={true}
        rightButtonText='만들기'
        onRightButtonClick={() => {
          console.log('생성');
        }}
      />
      <div className='w-100 px-8 mt-3'>
        <input
          type='text'
          ref={titleRef}
          value={title}
          maxLength={16}
          onChange={changeTitle}
          placeholder='제목을 입력해 주세요.'
          className='input w-full font-bold text-[20px] placeholder:font-extrabold placeholder:text-[20px] placeholder:text-gray-500'
        />
        <div className='border-b-2 border-gray-500'></div>
      </div>
      <div className='h-[25%] mx-8 mt-5 border border-gray-500 rounded-xl flex flex-col items-center justify-center overflow-hidden'>
        <PartyMap />
      </div>
      <div className='h-[20%] mx-8 mt-5'>
        <div className='w-[100%] h-[50%] flex justify-between'>
          <div className='w-[8%] h-[100%]'>
            <SvgDepartureMarker width={'100%'} height={'100%'} />
          </div>
          <div className='w-[85%]'>
            <div className='flex justify-between'>
              <div className='font-bold mb-1 text-[17px]'>{departuresName}</div>
              <div className='w-[10%] flex justify-center items-center'>
                <img src={Front} alt='출발지 설정' />
              </div>
            </div>
            <div className='text-gray-500'>출발지</div>
          </div>
        </div>
        <div className='w-[100%] h-[50%] flex justify-between'>
          <div className='w-[8%] h-[100%]'>
            <SvgArrivalMarker width={'100%'} height={'35px'} />
          </div>
          <div className='w-[85%]'>
            <div className='flex justify-between'>
              <div className='font-bold mb-1 text-[17px]'>도착지 상태변화값</div>
              <div className='w-[10%] flex justify-center items-center'>
                <img src={Front} alt='도착지 설정' />
              </div>
            </div>
            <div className='text-gray-500'>도착지</div>
          </div>
        </div>
      </div>
      <div className='border-b-[8px] border-gray-700'></div>
      <div className='h-[7%] mt-6 mx-8 flex flex-col justify-between'>
        <div className='font-bold text-gray-100 text-[18px]'>출발시간</div>
        <div className='flex justify-between'>
          <div className='text-gray-500'>탑승 일시를 선택해주세요.</div>
          <div className='w-[10%] flex justify-center items-center'>
            <img src={Front} alt='출발시간 설정' />
          </div>
        </div>
      </div>
      <div className=' h-[7%] mt-9 mx-8 flex flex-col justify-between'>
        <div className='font-bold text-gray-100 text-[18px]'>모집인원</div>
        <div className='flex justify-between'>
          <div className='text-gray-500'>모집 인원을 선택해 주세요</div>
          <div className='w-[10%] flex justify-center items-center'>
            <img src={Front} alt='출발시간 설정' />
          </div>
        </div>
      </div>
      <div className='h-[10%] mt-9 mx-8 flex flex-col justify-between'>
        <div className='font-bold text-gray-100 text-[18px]'>카테고리</div>
        <div className='flex gap-7 overflow-x-auto whitespace-nowrap'>
          {Category.map(({ tag, name }) => {
            switch (tag) {
              case 'DAILY':
                return (
                  <div key={tag}>
                    <div className='px-5 py-2 border border-OD_GREEN rounded-full text-OD_GREEN '>
                      {name}
                    </div>
                  </div>
                );
              case 'UNIVERSITY':
                return (
                  <div key={tag}>
                    <div className='px-5 py-2 border border-OD_JORDYBLUE rounded-full text-OD_JORDYBLUE'>
                      {name}
                    </div>
                  </div>
                );
              case 'COMMUTE':
                return (
                  <div key={tag}>
                    <div className='px-5 py-2 border border-OD_YELLOW rounded-full text-OD_YELLOW'>
                      {name}
                    </div>
                  </div>
                );
              case 'CONCERT':
                return (
                  <div key={tag}>
                    <div className='px-5 py-2 border border-OD_PINK rounded-full text-OD_PINK'>
                      {name}
                    </div>
                  </div>
                );
              case 'AIRPORT':
                return (
                  <div key={tag}>
                    <div className='px-5 py-2 border border-OD_SKYBLUE rounded-full text-OD_SKYBLUE'>
                      {name}
                    </div>
                  </div>
                );
              case 'TRAVEL':
                return (
                  <div key={tag}>
                    <div className='px-5 py-2 border border-OD_ORANGE rounded-full text-OD_ORANGE'>
                      {name}
                    </div>
                  </div>
                );
              case 'RESERVIST':
                return (
                  <div key={tag}>
                    <div className='px-5 py-2 border border-OD_SCARLET rounded-full text-OD_SCARLET'>
                      {name}
                    </div>
                  </div>
                );
            }
          })}
        </div>
      </div>
      <div className='h-[30%] mt-9 mx-8'>
        <div className='font-bold text-gray-100 text-[18px]'>파티 설명 (선택)</div>
        <label className='form-control'>
          <textarea
            className='textarea textarea-bordered min-h-32 resize-none mt-5'
            ref={contentRef}
            value={content}
            placeholder='자유롭게 파티를 설명해 주세요.'
            maxLength={100}
            onChange={changeContent}>
            {content}
          </textarea>
          <div className='label'>
            <span className='label-text-alt'></span>
            <span className='label-text-alt'>{content.length} / 100</span>
          </div>
        </label>
      </div>
    </div>
  );
};

export default PartyCreatePage;
