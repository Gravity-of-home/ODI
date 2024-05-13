import React, { useEffect, useState } from 'react';
import userStore from '@/stores/useUserStore';
import { useWebSocket } from '@/context/webSocketProvider';
import { getCookie } from '@/utils/CookieUtil';

const MatchPage: React.FC = () => {
  const { client, isConnected } = useWebSocket();
  //   const [messges, setMessages] = useState([]);
  const { id, name, nickname, email, ageGroup, gender, image, brix, logoutUser, Logout } =
    userStore();
  useEffect(() => {
    if (client && client.connected) {
      const subscription = client.subscribe(
        `/sub/matchResult`,
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
        destination: `/pub/match/3`,
        body: JSON.stringify({
          content: {
            depName: '미사역',
            depLon: '127.19267443618465',
            depLat: '37.563028890747944',
            arrName: '하남스타필드',
            arrLon: '127.22375912402354',
            arrLat: '37.54550375661386',
          },
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
      <button onClick={handleSendMessage}>PUB 하기</button>
    </div>
  );
};

export default MatchPage;
