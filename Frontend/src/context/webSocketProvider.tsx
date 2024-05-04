import React, { createContext, useContext, useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { getCookie } from '@/utils/CookieUtil';

interface WebSocketContextType {
  client: Client | null;
}

const WebSocketContext = createContext<WebSocketContextType | null>(null);

interface WebSocketProviderProps {
  children: React.ReactNode;
}

export const WebSocketProvider: React.FC<WebSocketProviderProps> = ({ children }) => {
  const [stompClient, setStompClient] = useState<Client | null>(null);

  useEffect(() => {
    const client = new Client({
      brokerURL: 'ws://localhost:8080/ws-stomp',
      // connectHeaders: {
      //   AUTHORIZATION: `Bearer ${getCookie('Authorization')}`,
      // },
      reconnectDelay: 5000,
      heartbeatIncoming: 20000, // 20 seconds
      heartbeatOutgoing: 20000, // 20 seconds
      webSocketFactory: () => new SockJS('http://localhost:8080/ws-stomp'),
      debug: str => {
        console.log('STOMP Debug:', str);
      },
      onConnect: () => {
        console.log(`WebSocket Connected`);
      },
      onDisconnect: () => {
        console.log(`WebSocket Disconnected`);
      },
      onStompError: frame => {
        console.error('Stomp Error:', frame.headers['message']);
      },
      onWebSocketError: err => {
        console.error('WebSocket Error:', err);
      },
    });

    client.activate();
    setStompClient(client);
  }, []);

  return (
    <WebSocketContext.Provider value={{ client: stompClient }}>
      {children}
    </WebSocketContext.Provider>
  );
};

export const useWebSocket = () => {
  const context = useContext(WebSocketContext);
  if (!context) {
    throw new Error('useWebSocket must be used within a WebSocketProvider');
  }
  return context.client;
};
