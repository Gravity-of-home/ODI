// Chat.js
import React, { useEffect, useState, useCallback } from 'react';
import { useParams } from 'react-router-dom';
import { useWebSocket } from '@/context/webSocketProvider';
import { getCookie } from '@/utils/CookieUtil';

interface IMessage {
  id: number;
  senderId: number;
  content: string;
  timestamp: string; // 메시지 수신 또는 발신 시간
}

const Chat = () => {
  const { partyId } = useParams<{ partyId: string }>();
  const { client, isConnected } = useWebSocket();
  const [messages, setMessages] = useState<IMessage[]>([
    { id: 1, senderId: 1, content: '안녕하세요, 반갑습니다!', timestamp: '12:45' },
    { id: 2, senderId: 2, content: '안녕하세요, 오늘 날씨가 참 좋네요.', timestamp: '12:46' },
    { id: 3, senderId: 1, content: '그러게요! 산책하기 딱 좋은 날이죠.', timestamp: '12:47' },
    { id: 4, senderId: 2, content: '다음에 함께 산책 가시죠!', timestamp: '12:48' },
    { id: 5, senderId: 1, content: '좋아요! 기대하겠습니다.', timestamp: '12:49' },
    ...Array.from({ length: 100 }, (_, index) => ({
      id: index + 6,
      senderId: index % 2 === 0 ? 1 : 2,
      content: `메시지 내용 ${index + 6}`,
      timestamp: `12:${50 + (index % 10)}`,
    })),
  ]);
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
        `/sub/chat/room/1`,
        message => {
          console.log(message);
          // const newMessage = JSON.parse(message.body).message;
          // setMessages(prevMessages => [...prevMessages, newMessage]);
        },
        {
          AUTHORIZATION: `Bearer ${getCookie('Authorization')}`,
          token: `Bearer ${getCookie('Authorization')}`,
          body: JSON.stringify({
            AUTHORIZATION: `Bearer ${getCookie('Authorization')}`,
            token: `Bearer ${getCookie('Authorization')}`,
            content: inputMessage,
            senderId: 1,
            messageType: 'TALK',
            party: 1,
          }),
        },
      );

      // return () => subscription.unsubscribe();
    }
  }, [client, isConnected]);

  const handleSendMessage = () => {
    if (client && client.connected) {
      client.publish({
        destination: `/pub/chat/message`,
        body: JSON.stringify({
          content: inputMessage,
          senderId: 1,
          messageType: 'TALK',
          party: { partyId },
        }),
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
    <div className='flex flex-col'>
      <div className='h-96 overflow-y mb-12'>
        {messages.map(msg =>
          msg.senderId === myId ? (
            <div className='chat chat-end' key={msg.id}>
              <div className='chat-header'>
                {myNickName}
                <time className='text-xs opacity-50'>12:46</time>
              </div>
              <div className='chat-bubble'>{msg.content}</div>
              <div className='chat-footer opacity-50'>Seen at 12:46</div>
            </div>
          ) : (
            <div className='chat chat-start'>
              <div className='chat-image avatar'>
                <div className='w-10 rounded-full'>
                  <img
                    alt='Tailwind CSS chat bubble component'
                    src='https://img.daisyui.com/images/stock/photo-1534528741775-53994a69daeb.jpg'
                  />
                </div>
              </div>
              <div className='chat-header'>
                Obi-Wan Kenobi
                <time className='text-xs opacity-50'>12:45</time>
              </div>
              <div className='chat-bubble'>You were the Chosen One!</div>
              <div className='chat-footer opacity-50'>Delivered</div>
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
          className='input w-full max-w-xs'
        />
        <button className='btn' onClick={handleSendMessage}>
          전송
        </button>
      </div>
    </div>
  );
};

export default Chat;
