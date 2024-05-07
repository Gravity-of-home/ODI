// Chat.js
import React, { useEffect, useState, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { useWebSocket } from '@/context/webSocketProvider';
import { getCookie } from '@/utils/CookieUtil';

const Chat = () => {
  const { partyId } = useParams<{ partyId: string }>();
  const { client, isConnected } = useWebSocket();
  const [messages, setMessages] = useState<string[]>([]);
  const [inputMessage, setInputMessage] = useState('');

  useEffect(() => {
    if (client && client.connected) {
      const subscription = client.subscribe(
        `/sub/chat/message`,
        message => {
          const newMessage = JSON.parse(message.body).message;
          setMessages(prevMessages => [...prevMessages, newMessage]);
        },
        {
          AUTHORIZATION: `Bearer ${getCookie('Authorization')}`,
          token: `${getCookie('Authorization')}`,
        },
      );

      return () => subscription.unsubscribe();
    }
  }, [client, isConnected]);

  const handleSendMessage = () => {
    if (client && client.connected) {
      client.publish({
        destination: `/pub/chat/message`,
        body: JSON.stringify({ content: inputMessage, senderId: 1, messageType: 'TALK', party: 1 }),
        headers: {
          AUTHORIZATION: `Bearer ${getCookie('Authorization')}`,
          token: `${getCookie('Authorization')}`,
          body: JSON.stringify({
            content: inputMessage,
            senderId: 1,
            messageType: 'TALK',
            party: 1,
          }),
        },
      });
      setInputMessage('');
    } else {
      alert('서버와의 연결이 끊어졌습니다. 잠시 후 다시 시도해주세요.');
    }
  };

  return (
    <div className=''>
      <div className='h-48'>
        {messages.map((msg, index) => (
          <div key={index}>{msg}</div>
        ))}
      </div>
      <div>
        <input
          type='text'
          value={inputMessage}
          onChange={e => setInputMessage(e.target.value)}
          onKeyPress={e => {
            if (e.key === 'Enter') {
              handleSendMessage();
            }
          }}
          placeholder='메세지를 입력하세요'
          className='input w-full max-w-xs'
        />
        <button onClick={handleSendMessage}>Send</button>
      </div>
    </div>
  );
};

export default Chat;
