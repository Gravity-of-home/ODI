// Chat.js
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { useWebSocket } from '@/context/webSocketProvider';
import { getCookie } from '@/utils/CookieUtil';
import { IUser, IMessage } from '@/types/Chat';

interface ChatProps {
  roomId: string;
  me: IUser;
  fetchData: () => void;
}

const Chat: React.FC<ChatProps> = ({ roomId, me, fetchData }) => {
  const { partyId } = useParams();
  const { client, isConnected } = useWebSocket();
  const [messages, setMessages] = useState<IMessage[]>([]);
  const [inputMessage, setInputMessage] = useState('');

  useEffect(() => {
    if (client && client.connected) {
      const subscription = client.subscribe(
        `/sub/chat/room/${roomId}`,
        message => {
          console.log(JSON.parse(message.body));

          const newMessage = JSON.parse(message.body);
          console.log(newMessage.type);
          if (['SETTLEMENT', 'ENTER', 'QUIT'].includes(newMessage.type)) {
            fetchData();
          }

          setMessages(prevMessages => [...prevMessages, newMessage]);
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
      if (inputMessage.trim()) {
        client.publish({
          destination: `/pub/chat/message`,
          body: JSON.stringify({
            partyId: partyId,
            roomId: roomId,
            content: inputMessage,
            type: 'TALK',
          }),
          headers: {
            token: `${getCookie('Authorization')}`,
          },
        });
        setInputMessage('');
      }
    } else {
      alert('서버와의 연결이 끊어졌습니다. 잠시 후 다시 시도해주세요.');
    }
  };

  return (
    <div className='flex flex-col'>
      <div className='h-96 overflow-y mb-12 p-4'>
        {messages.map(msg =>
          msg.type === 'TALK' ? (
            msg.senderNickname === me.nickname ? (
              <div className='chat chat-end' key={msg.timestamp}>
                <div className='chat-header'>
                  <time className='text-xs opacity-50'>{msg.sendTime}</time>
                </div>
                <div className='chat-bubble'>{msg.content}</div>
              </div>
            ) : (
              <div className='chat chat-start' key={msg.timestamp}>
                <div className='chat-image avatar'>
                  <div className='w-10 rounded-full'>
                    <img alt='User avatar' src={msg.senderImage} />
                  </div>
                </div>
                <div className='chat-header'>
                  {msg.senderNickname}
                  <time className='text-xs opacity-50'>{msg.sendTime}</time>
                </div>
                <div className='chat-bubble'>{msg.content}</div>
              </div>
            )
          ) : (
            <div className='chat chat-start' key={msg.timestamp}>
              <div className='chat-image avatar'>
                <div className='w-10 rounded-full'>
                  <img alt='User avatar' src={msg.senderImage} />
                </div>
              </div>
              <div className='chat-header'>
                {msg.senderNickname}
                <time className='text-xs opacity-50'>{msg.sendTime}</time>
              </div>
              <div className='chat-bubble'>{msg.content}</div>
            </div>
          ),
        )}
      </div>

      <div className='fixed bottom-0 bg-white flex w-screen'>
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
          className='input w-full'
        />
        <button className='btn' onClick={handleSendMessage}>
          전송
        </button>
      </div>
    </div>
  );
};

export default Chat;
