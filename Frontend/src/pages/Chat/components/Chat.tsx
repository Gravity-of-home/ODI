// Chat.js
import React, { useEffect, useState, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { useWebSocket } from '@/context/webSocketProvider';
import { getCookie } from '@/utils/CookieUtil';

interface IMessage {
  senderNickname: string;
  content: string;
  timestamp: string; // 메시지 수신 또는 발신 시간
  senderImage: string;
  sendTime: string;
  type: string;
}
interface ChatProps {
  roomId: string | undefined;
}

const Chat: React.FC<ChatProps> = ({ roomId }) => {
  const { partyId } = useParams();
  const { client, isConnected } = useWebSocket();
  const [messages, setMessages] = useState<IMessage[]>([]);
  const [inputMessage, setInputMessage] = useState('');
  const [myNickName, setMyNickName] = useState('');
  const [myId, setMyID] = useState(0);

  useEffect(() => {
    const userDataJSON = localStorage.getItem('User');

    if (userDataJSON) {
      const userData = JSON.parse(userDataJSON);
      const nickname = userData?.state?.nickname;
      const id = userData?.state?.id;
      setMyNickName(nickname);
      setMyID(id);
    }
  }, []);

  useEffect(() => {
    if (client && client.connected) {
      const subscription = client.subscribe(
        `/sub/chat/room/${roomId}`,
        message => {
          console.log(JSON.parse(message.body));
          const newMessage = JSON.parse(message.body);
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
      } else {
      }
    } else {
      alert('서버와의 연결이 끊어졌습니다. 잠시 후 다시 시도해주세요.');
    }
  };

  return (
    <div className='flex flex-col'>
      <div className='h-96 overflow-y mb-12 p-4'>
        {messages.map(msg =>
          msg.type === 'TALK' && msg.senderNickname === myNickName ? (
            <div className='chat chat-end'>
              <div className='chat-header'>
                <time className='text-xs opacity-50'>{msg.sendTime}</time>
              </div>
              <div className='chat-bubble'>{msg.content}</div>
              {/* <div className='chat-footer opacity-50'>Seen at 12:46</div> */}
            </div>
          ) : (
            <div className='chat chat-start'>
              <div className='chat-image avatar'>
                <div className='w-10 rounded-full'>
                  <img alt='Tailwind CSS chat bubble component' src={msg.senderImage} />
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
