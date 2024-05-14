import React, { useEffect, useState } from 'react';
import userStore from '@/stores/useUserStore';
import { useWebSocket } from '@/context/webSocketProvider';
import { getCookie } from '@/utils/CookieUtil';

const MatchPage: React.FC = () => {
  const { client, isConnected } = useWebSocket();
  //   const [messges, setMessages] = useState([]);
  const { id, name, nickname, email, ageGroup, gender, image, brix, logoutUser, Logout } =
    userStore();

  // 상태 생성
  const [depName, setDepName] = useState('');
  const [depLon, setDepLon] = useState('');
  const [depLat, setDepLat] = useState('');
  const [arrName, setArrName] = useState('');
  const [arrLon, setArrLon] = useState('');
  const [arrLat, setArrLat] = useState('');

  useEffect(() => {
    if (client && client.connected) {
      const subscription = client.subscribe(
        `/sub/matchResult/${id}`,
        message => {
          console.log(JSON.parse(message.body));

          const newMessage = JSON.parse(message.body);
          console.log(newMessage.type);
        },
        {
          token: `${getCookie('Authorization')}`,
        },
      );

      return () => subscription.unsubscribe();
    }
  }, [client, isConnected]);

  const handleSendMessage = () => {
    if (client && client.connected) {
      client.publish({
        destination: `/pub/match/${id}`,
        body: JSON.stringify({
          depName,
          depLon,
          depLat,
          arrName,
          arrLon,
          arrLat,
        }),
        headers: {
          token: `${getCookie('Authorization')}`,
        },
      });
    } else {
      alert('서버와의 연결이 끊어졌습니다. 잠시 후 다시 시도해주세요.');
    }
  };

  const testPub = () => {
    if (client && client.connected) {
      client.publish({
        destination: `/pub/notification/`,
        body: JSON.stringify({
          receiverId: 1,
          partyId: 1,
          content: '방씀다 ^^',
          type: 'APPLY',
        }),
        headers: {
          token: `${getCookie('Authorization')}`,
        },
      });
    } else {
      alert('서버와의 연결이 끊어졌습니다. 잠시 후 다시 시도해주세요.');
    }
  };

  return (
    <div className='flex flex-col'>
      <input
        type='text'
        value={depName}
        onChange={e => setDepName(e.target.value)}
        placeholder='출발지 이름'
      />
      <input
        type='text'
        value={depLon}
        onChange={e => setDepLon(e.target.value)}
        placeholder='출발지 경도'
      />
      <input
        type='text'
        value={depLat}
        onChange={e => setDepLat(e.target.value)}
        placeholder='출발지 위도'
      />
      <input
        type='text'
        value={arrName}
        onChange={e => setArrName(e.target.value)}
        placeholder='도착지 이름'
      />
      <input
        type='text'
        value={arrLon}
        onChange={e => setArrLon(e.target.value)}
        placeholder='도착지 경도'
      />
      <input
        type='text'
        value={arrLat}
        onChange={e => setArrLat(e.target.value)}
        placeholder='도착지 위도'
      />
      <button onClick={handleSendMessage}>PUB 하기</button>
      <button onClick={testPub}>개인알림 테스트</button>
    </div>
  );
};

export default MatchPage;
