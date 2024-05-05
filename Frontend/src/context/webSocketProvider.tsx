import React, { createContext, useContext, useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

const WebSocketContext = createContext<Client | null>(null);

interface WebSocketProviderProps {
  children: React.ReactNode;
  partyId: string | undefined; // partyId를 props로 추가
}

export const WebSocketProvider: React.FC<WebSocketProviderProps> = ({ children, partyId }) => {
  const [stompClient, setStompClient] = useState<Client | null>(null);

  useEffect(() => {
    console.log('provider attached');
    const sockJS = new SockJS(`http://localhost:8080/ws-stomp`); // partyId를 사용하는 WebSocket URL
    const client = new Client({
      webSocketFactory: () => new SockJS(`http://localhost:8080/ws-stomp`),
      reconnectDelay: 5000,
      debug: str => {
        console.log('STOMP Debug:', str);
      },
      onConnect: () => {
        console.log(`WebSocket Connected for party ${partyId}`);
      },
      onDisconnect: () => {
        console.log(`WebSocket Disconnected for party ${partyId}`);
      },
      onStompError: frame => {
        console.error('Stomp Error:', frame.headers['message']);
      },
      onWebSocketError: err => {
        console.error('WebSocket Error:', err);
      },
    });
    setStompClient(client);
    client.activate();
    return () => {
      console.log('client deactivate');
      client.deactivate();
    };
  }, []); // partyId에 의존하는 useEffect

  return <WebSocketContext.Provider value={stompClient}>{children}</WebSocketContext.Provider>;
};

export const useWebSocket = () => useContext(WebSocketContext);
