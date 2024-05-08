import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import NavBar from './components/NavBar';
import jwtAxios from '@/utils/JWTUtil';
import { getCookie } from '@/utils/CookieUtil';
import Info from './components/Info';
import Chat from './components/Chat';
import { WebSocketProvider } from '../../context/webSocketProvider';

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

        // setHostInfo(FindHost(res.data.data.participants));
      })
      .catch(err => {
        if (err instanceof Error) {
          setError(err.message);
        } else {
          setError('An unknown error occurred');
        }
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
  if (error) return <p>{error}에런디유? 다시 시도해봐유 </p>;
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
