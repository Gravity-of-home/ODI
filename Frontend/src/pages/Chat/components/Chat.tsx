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

interface ChatMessageProps {
  msg: IMessage;
  isOwnMessage: boolean;
  showImage: boolean;
  showTime: boolean;
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
          const newMessage = JSON.parse(message.body);

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
  const isDifferentTime = (time1: string, time2: string) => {
    const date1 = new Date(time1);
    const date2 = new Date(time2);
    return (
      date1.getFullYear() !== date2.getFullYear() ||
      date1.getMonth() !== date2.getMonth() ||
      date1.getDate() !== date2.getDate() ||
      date1.getHours() !== date2.getHours() ||
      date1.getMinutes() !== date2.getMinutes()
    );
  };

  const ChatMessage: React.FC<ChatMessageProps> = ({ msg, isOwnMessage, showImage, showTime }) => (
    <div className={`chat ${isOwnMessage ? 'chat-end' : 'chat-start'}`}>
      {!isOwnMessage && (
        <div className='chat-image avatar'>
          {showImage ? (
            <div className='w-10 rounded-full'>
              <img alt='img' src={msg.senderImage} />
            </div>
          ) : (
            <div className='w-10 rounded-full'></div>
          )}
        </div>
      )}
      <div
        className={`chat-bubble ${isOwnMessage ? 'chat-bubble-primary' : 'chat-bubble-secondary'}`}>
        {!isOwnMessage && <div className='chat-header'>{msg.senderNickname}</div>}
        <p className='break-words'>{msg.content}</p>
      </div>
      {showTime && <time className='chat-footer opacity-50'>{NewTimeFormat(msg.sendTime)}</time>}
    </div>
  );

  return (
    <div className='flex flex-col'>
      <div className='mb-12 p-4 flex-grow'>
        {messages.map((msg, index) => {
          const prevMsg = messages[index - 1];
          const nextMsg = messages[index + 1];
          const showImage =
            !prevMsg ||
            prevMsg.senderNickname !== msg.senderNickname ||
            prevMsg?.type !== 'TALK' ||
            isDifferentTime(prevMsg.sendTime, msg.sendTime);
          const showTime =
            !nextMsg ||
            nextMsg.senderNickname !== msg.senderNickname ||
            nextMsg?.type !== 'TALK' ||
            isDifferentTime(msg.sendTime, nextMsg.sendTime);

          return msg.type === 'TALK' ? (
            <ChatMessage
              key={index}
              msg={msg}
              isOwnMessage={msg.senderNickname === me.nickname}
              showImage={showImage}
              showTime={showTime}
            />
          ) : (
            <div key={index} className='flex justify-center my-4'>
              <span className='badge badge-lg'>{msg.content}</span>
            </div>
          );
        })}
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
