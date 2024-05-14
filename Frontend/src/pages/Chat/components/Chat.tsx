// Chat.js
import React, { useEffect, useState, useRef } from 'react';
import { useParams } from 'react-router-dom';
import { useWebSocket } from '@/context/webSocketProvider';
import { getCookie } from '@/utils/CookieUtil';
import { IUser, IMessage } from '@/types/Chat';
import jwtAxios from '@/utils/JWTUtil';

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
  const messagesEndRef = useRef<HTMLDivElement>(null);
  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(scrollToBottom, [messages]);
  function fetchMessages() {
    jwtAxios
      .get(`api/chat/room/${roomId}`)
      .then(res => {
        const beforeChat = res.data.chatMessages;
        console.log(beforeChat);
        setMessages(prevMessages => [...prevMessages, ...beforeChat]); // 배열을 펼쳐서 추가
      })
      .catch(error => {
        console.error('Error fetching messages:', error);
      });
  }

  useEffect(() => {
    if (client && client.connected) {
      fetchMessages();
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

  function NewTimeFormat(time: string) {
    const date = new Date(time);
    const hour = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');

    const newTimeString = `${hour}:${minutes}`;

    return newTimeString;
  }

  return (
    <div className='flex flex-col'>
      <div className='mb-12 p-4 flex-grow'>
        {messages.map((msg, index) =>
          msg.type === 'TALK' ? (
            msg.senderNickname === me.nickname ? (
              <div className='chat chat-end'>
                <div className='chat-header'>{msg.senderNickname}</div>
                <div className='chat-bubble'>{msg.content}</div>
                <time className='chat-footer opacity-50'>{NewTimeFormat(msg.sendTime)}</time>
                {/* <div className='chat-footer opacity-50'>{msg.sendTime}</div> */}
              </div>
            ) : (
              <div className='chat chat-start'>
                <div className='chat-image avatar'>
                  <div className='w-10 rounded-full'>
                    <img alt='img' src={msg.senderImage} />
                  </div>
                </div>
                <div className='chat-header'>
                  {msg.senderNickname}
                  {/* <time className='text-xs opacity-50'>{msg.sendTime}</time> */}
                </div>
                <div className='chat-bubble'>{msg.content}</div>
                <time className='chat-footer opacity-50'>{NewTimeFormat(msg.sendTime)}</time>
                {/* <div className='chat-footer opacity-50'>Delivered</div> */}
              </div>
            )
          ) : (
            <div key={index} className='flex justify-center my-4'>
              <span className='badge badge-lg'>{msg.content}</span>
            </div>
          ),
        )}
        <div ref={messagesEndRef} />
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
