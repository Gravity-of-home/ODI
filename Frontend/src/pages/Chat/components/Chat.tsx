import React, { useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { useWebSocket } from '@/context/webSocketProvider';

import jwtAxios from '@/utils/JWTUtil';

const Chat = () => {
  const stompClient = useWebSocket();
  const { partyId } = useParams();
  // const sendMessage = msg => {
  //   if (stompClient) {
  //     stompClient.publish('/app/send/message', {}, JSON.stringify(msg));
  //   }
  // };
  function fetchData() {
    jwtAxios.get(`api/`);
  }
  return (
    <div>
      <div className='chat'>Chat content will appear here</div>
    </div>
  );
};

export default Chat;
