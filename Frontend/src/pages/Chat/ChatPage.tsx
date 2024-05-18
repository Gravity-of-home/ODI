import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import jwtAxios from '@/utils/JWTUtil';
import { getCookie } from '@/utils/CookieUtil';
import NavBar from './components/NavBar';
import Chat from './components/Chat';
import { IChatInfo } from '@/types/Chat';

const ChatPage = () => {
  const { partyId } = useParams();
  const [info, setInfo] = useState<IChatInfo>();
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
  }, []);
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
    <div className='chat-page max-h-full flex flex-col'>
      {info && (
        <div className='fixed top-0 bg-white w-screen z-10'>
          <NavBar
            info={info}
            title={info.title}
            departuresName={info.departuresName}
            arrivalsName={info.arrivalsName}
            departuresDate={info.departuresDate}
            state={info.state}
            me={info.me}
            roomId={info.roomId}
            fetchData={fetchData}
          />
        </div>
      )}
      <div className='flex-grow overflow-y-auto' style={{ paddingTop: '10rem' }}>
        {info && <Chat roomId={info.roomId} me={info.me} fetchData={fetchData} />}
      </div>
    </div>
  );
};

export default ChatPage;
