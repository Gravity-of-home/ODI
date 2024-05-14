import React, { createContext, useContext, useEffect, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { getCookie } from '@/utils/CookieUtil';
import { ViteConfig } from '@/apis/ViteConfig';
import axios from 'axios';
import userStore from '@/stores/useUserStore';
// 타입 정의
interface WebSocketContextType {
  client: Client | null;
  isConnected: boolean;
}

const WebSocketContext = createContext<WebSocketContextType | null>(null);

// 프로퍼티 타입 정의
interface WebSocketProviderProps {
  children: React.ReactNode;
}

export const WebSocketProvider: React.FC<WebSocketProviderProps> = ({ children }) => {
  const [stompClient, setStompClient] = useState<Client | null>(null);
  const [isConnected, setIsConnected] = useState(false); // 연결 상태를 추적하는 상태
  const BASE_URI = ViteConfig.VITE_BASE_URL;
  const broker = ViteConfig.VITE_SOCK_URL;
  const { id } = userStore();

  useEffect(() => {
    const token = getCookie('Authorization');
    if (!token) {
      console.error('Authorization token is missing');
      return;
    }

    // WebSocket 및 STOMP 클라이언트 설정
    const client = new Client({
      brokerURL: `${broker}`,
      connectHeaders: {
        AUTHORIZATION: `Bearer ${token}`,
        token: `${token}`,
      },
      heartbeatIncoming: 20000, // 20초
      heartbeatOutgoing: 20000, // 20초
      webSocketFactory: () => new SockJS(`${BASE_URI}/ws-stomp`),
      // debug: str => console.log('STOMP Debug:', str),
      onConnect: () => {
        console.log('Client CONNECT');
        setIsConnected(true);
        // TODO 개인별 알림 구독
        client.subscribe(
          '',
          message => {
            console.log(message);
          },
          {
            token: `${getCookie('Authorization')}`,
          },
        );
      },
      onDisconnect: async () => {
        console.log('WebSocket Disconnected');
        setIsConnected(false); // 연결 해제시 상태 업데이트
        /**
         * @description
         * onDisconnect: 연결이 끊어진 경우 호출되는 콜백 함수
         * 1. 서버 또는 네트워크 문제로 인해 연결이 끊어질 때 호출
         * 2. 클라이언트에서 명시적으로 연결을 종료할 때
         * deactivate() 메서드와의 관계
         * 1. deactivate() 메서드를 호출하면 onDisconnect() 콜백 함수가 호출
         */
        try {
          // QUESTION: 자동 매칭이 아닌 다른 곳에서 요청에도 실행된다.
          const response = await axios.delete(`${ViteConfig.VITE_BASE_URL}/api/matches/${id}`, {
            headers: {
              AUTHORIZATION: `Bearer ${token}`,
            },
          });

          console.log(response);
        } catch (err) {
          console.error(err);
        }
      },
      onStompError: frame => {
        console.error('Stomp Error:', frame.headers['message']);
        setIsConnected(false); // 에러 발생시 연결 상태 업데이트
      },
      onWebSocketError: err => {
        console.error('WebSocket Error:', err);
        setIsConnected(false); // 에러 발생시 연결 상태 업데이트
      },
    });

    setStompClient(client);
    client.activate();

    // 클라이언트 비활성화 및 정리
    return () => {
      client.deactivate();
    };
  }, []);

  return (
    <WebSocketContext.Provider value={{ client: stompClient, isConnected }}>
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
  return context;
};
