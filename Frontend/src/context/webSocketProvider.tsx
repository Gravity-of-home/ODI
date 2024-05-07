import React, { createContext, useContext, useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { getCookie } from '@/utils/CookieUtil';

// 타입 정의
interface WebSocketContextType {
  client: Client | null;
}

const WebSocketContext = createContext<WebSocketContextType | null>(null);

// 프로퍼티 타입 정의
interface WebSocketProviderProps {
  children: React.ReactNode;
}

export const WebSocketProvider: React.FC<WebSocketProviderProps> = ({ children }) => {
  const [stompClient, setStompClient] = useState<Client | null>(null);

  useEffect(() => {
    const token = getCookie('Authorization');
    if (!token) {
      console.error('Authorization token is missing');
      return;
    }

    // WebSocket 및 STOMP 클라이언트 설정
    const client = new Client({
      brokerURL: `ws://localhost:8080/ws-stomp`,
      brokerURL: 'ws://localhost:8080/ws-stomp',
      connectHeaders: {
        AUTHORIZATION: `Bearer ${token}`,
      },
      heartbeatIncoming: 20000, // 20초
      heartbeatOutgoing: 20000, // 20초
      webSocketFactory: () => new SockJS('http://localhost:8080/ws-stomp'),
      debug: str => console.log('STOMP Debug:', str),
      onConnect: () => console.log('WebSocket Connected'),
      onDisconnect: () => console.log('WebSocket Disconnected'),
      onStompError: frame => console.error('Stomp Error:', frame.headers['message']),
      onWebSocketError: err => console.error('WebSocket Error:', err),
    });

    setStompClient(client);
    client.activate();

    // 클라이언트 비활성화 및 정리
    return () => {
      client.deactivate();
    };
  }, []);

  return (
    <WebSocketContext.Provider value={{ client: stompClient }}>
      {children}
    </WebSocketContext.Provider>
  );
};

// 컨텍스트 훅
export const useWebSocket = () => {
  const context = useContext(WebSocketContext);
  if (!context) {
    throw new Error('useWebSocket must be used within a WebSocketProvider');
  }
  return context.client;
};
