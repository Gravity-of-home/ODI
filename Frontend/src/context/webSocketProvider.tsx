import React, { createContext, useContext, useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client, IStompSocket } from '@stomp/stompjs';
import { getCookie } from '@/utils/CookieUtil';

const WebSocketContext = createContext({ client: new Client() });

interface WebSocketProviderProps {
  children: React.ReactNode;
}

export const WebSocketProvider: React.FC<WebSocketProviderProps> = ({ children }) => {
  const [client, setClient] = useState<Client>(new Client());

  useEffect(() => {
    if (!getCookie('Authorization')) {
      return;
    }

    console.log('provider attached');
    console.log('Authorization:', getCookie('Authorization'));

    // partyId를 사용하는 WebSocket URL
    const newClient = new Client({
      brokerURL: `ws://localhost:8080/ws-stomp`,
      connectHeaders: { AUTHORIZATION: `Bearer ${getCookie('Authorization')}` },
      reconnectDelay: 5000,
      heartbeatIncoming: 0,
      heartbeatOutgoing: 0,
      debug: str => {
        console.log('STOMP Debug:', str);
      },
      onConnect: () => {
        console.log(`WebSocket Connected`);
      },
      onDisconnect: () => {
        console.log(`WebSocket Disconnected`);
      },
      onWebSocketClose: res => {
        console.log('WebSocket Close', res);
      },
      onWebSocketError: err => {
        console.log('WebSocket Error', err);
      },
    });

    newClient.webSocketFactory = () => {
      return new SockJS(`http://localhost:8080/ws-stomp`) as IStompSocket;
    };
    setClient(newClient);
    newClient.activate();
    return () => {
      console.log('client deactivate');
      newClient.deactivate();
    };
  }, []); // partyId에 의존하는 useEffect

  return <WebSocketContext.Provider value={{ client }}>{children}</WebSocketContext.Provider>;
};

export const useWebSocket = () => {
  const context = useContext(WebSocketContext);
  return context;
};
