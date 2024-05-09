import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import jwtAxios from '@/utils/JWTUtil';
import { getCookie } from '@/utils/CookieUtil';
import { WebSocketProvider } from '../../context/webSocketProvider';
import NavBar from './components/NavBar';
import Chat from './components/Chat';

interface IInfo {
  partyId: number;
  title: string;
  currentParticipants: number;
  departuresName: string;
  arrivalsName: string;
  departuresDate: string;
  state: string;
  me: {
    id: number;
    role: string;
    nickname: string;
    gender: string;
    ageGroup: string;
    profileImage: string;
    isPaid: boolean;
  };
  organizer: {
    id: number;
    role: string;
    nickname: string;
    gender: string;
    ageGroup: string;
    profileImage: string;
    isPaid: boolean;
  };
  participants: {
    id: number;
    role: string;
    nickname: string;
    gender: string;
    ageGroup: string;
    profileImage: string;
    isPaid: boolean;
  }[];
}

const ChatPage = () => {
  const { partyId } = useParams();
  const [info, setInfo] = useState<IInfo>();
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState('');

  const navigate = useNavigate();
  function goBack() {
    navigate(`/home`);
  }

  const fetchData = async () => {
    await jwtAxios
      .get(`api/party-boards/${partyId}/chat-info`, {
        headers: {
          AUTHORIZATION: `Bearer ${getCookie('Authorization')}`,
        },
      })
      .then(res => {
        console.log(res);
        setInfo(res.data.data);
      })
      .catch(err => {
        console.error(err);
        setError(err.response.data.message);
      });

    setIsLoading(false);
  };

  useEffect(() => {
    fetchData();
  }, []); // 이제 `fetchData`에 대한 의존성이 명시적
  if (isLoading)
    return (
      <div className='flex h-screen justify-center'>
        <span className='loading loading-ball loading-xs'></span>
        <span className='loading loading-ball loading-sm'></span>
        <span className='loading loading-ball loading-md'></span>
        <span className='loading loading-ball loading-lg'></span>
      </div>
    );
  if (error)
    return (
      <div role='alert' className='alert alert-error bg-white h-screen content-center'>
        <svg
          xmlns='http://www.w3.org/2000/svg'
          className='stroke-current shrink-0 h-12 w-12 text-red-600'
          fill='none'
          viewBox='0 0 24 24'>
          <path
            strokeLinecap='round'
            strokeLinejoin='round'
            strokeWidth='2'
            d='M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z'
          />
        </svg>
        <div>
          <h3 className='font-bold'>Error!</h3>
          <div className='text-xs'>{error}</div>
        </div>
        <button onClick={goBack} className='btn btn-sm'>
          돌아가기
        </button>
      </div>
    );
  return (
    <WebSocketProvider>
      <div className='chat-page'>
        <div className=''>
          {info && (
            <NavBar
              title={info.title}
              departuresName={info.departuresName}
              arrivalsName={info.arrivalsName}
              departuresDate={info.departuresDate}
              state={info.state}
              me={info.me}
              fetchData={fetchData}
            />
          )}
        </div>

        <div className='divider'></div>

        <div className='flex-1 overflow-y mt-20'>
          <Chat />
        </div>
      </div>
    </WebSocketProvider>
  );
};

export default ChatPage;
