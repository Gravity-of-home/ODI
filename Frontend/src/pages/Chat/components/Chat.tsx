import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useWebSocket } from '@/context/webSocketProvider';

const Chat = () => {
  const { partyId } = useParams<{ partyId: string }>();
  const stompClient = useWebSocket();
  const [messages, setMessages] = useState<string[]>([]);

  useEffect(() => {
    // 연결 상태를 확인하고 구독을 설정하는 함수를 정의합니다.
    const setupSubscription = () => {
      if (stompClient && stompClient.connected) {
        const subscription = stompClient.subscribe(`/sub/chat/room/${partyId}`, message => {
          const newMessage = JSON.parse(message.body).message;
          console.log(newMessage); // 개발 중에는 로그를 확인할 수 있지만, 프로덕션에서는 제거하는 것이 좋습니다.
          setMessages(prevMessages => [...prevMessages, newMessage]);
        });

        return () => subscription.unsubscribe(); // 구독 해제
      }
    };

    // 클라이언트가 연결될 때 구독을 설정합니다.
    const subscription = setupSubscription();
    return () => {
      // 컴포넌트 언마운트 시 구독을 해제합니다.
      if (subscription) {
        subscription();
      }
    };
  }, [stompClient, partyId]);

  // 메시지 표시 부분은 따로 컴포넌트로 분리할 수도 있습니다.
  return <div className='chat'>{messages}</div>;
};

export default Chat;
