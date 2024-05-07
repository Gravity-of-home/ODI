import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import jwtAxios from '@/utils/JWTUtil';

const ChatDetailPage = () => {
  const [error, setError] = useState(false);
  const nav = useNavigate();

  function fetchData() {
    const response = jwtAxios
      .post(`api/chats`)
      .then(res => {
        console.log(res.data);
      })
      .catch(err => {
        console.log(err);
      });
  }

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div className='container'>
      <button className='btn btn-ghost'>채팅방 나가기</button>
    </div>
  );
};

export default ChatDetailPage;
