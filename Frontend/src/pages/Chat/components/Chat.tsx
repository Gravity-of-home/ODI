import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useWebSocket } from '@/context/webSocketProvider';

const Chat = () => {
  const { partyId } = useParams<{ partyId: string }>();
  const stompClient = useWebSocket();
  const [messages, setMessages] = useState<string[]>([]);

  useEffect(() => {
    if (stompClient) {
      // onConnect 내에서 구독 로직을 설정
      const onConnected = () => {
        console.log('Connected to the WebSocket');
        const subscription = stompClient.subscribe(`/sub/chat/room/${partyId}`, message => {
          const newMessage = JSON.parse(message.body).message;
          console.log(newMessage);
          setMessages(prevMessages => [...prevMessages, newMessage]);
        });

        stompClient.subscribe(`/sub`, message => {
          console.log(message);
        });

        return () => {
          subscription.unsubscribe(); // Clean up the subscription when the component unmounts or dependencies change
        };
      };

      // 클라이언트 연결 및 구독
      if (!stompClient.active) {
        stompClient.activate();
        stompClient.onConnect = onConnected;
      }
    }
  }, [stompClient, partyId]); // stompClient 및 partyId에 의존하는 useEffect

  return (
    <div>
      <div className='chat'>
        {messages.map((msg, index) => (
          <div key={index}>{msg}</div> // 메시지 출력
        ))}
      </div>
    </div>
  );
};

export default Chat;
