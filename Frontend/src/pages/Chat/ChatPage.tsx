import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import NavBar from './components/NavBar';
import axios from 'axios';
import Info from './components/Info';
import Chat from './components/Chat';
import { WebSocketProvider } from '../../context/webSocketProvider';

// TODO
//1. stomp 연결
//2. 채팅을 어떻게 불러올건가?
//3. 정산을 여기서 해야하는디 어떻게 만들어야할까
const ChatPage = () => {
  const { partyId } = useParams();

  // 채팅기록 불러와야겠지?
  function fetchData() {
    const response = axios
      .post(`http://localhost:8080/api/parties/${partyId}`)
      .then(res => {
        console.log(res.data);
      })
      .catch(err => {
        console.log(err);
      });
  }

  return (
    <WebSocketProvider>
      <div className='chat-page'>
        <div className='nav'>
          <NavBar />
        </div>
        <div>
          <Info />
        </div>
        <div>
          <Chat />
        </div>
      </div>
    </WebSocketProvider>
  );
};

export default ChatPage;
