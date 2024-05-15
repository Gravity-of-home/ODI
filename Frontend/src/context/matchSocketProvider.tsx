import React, { createContext, useContext, useEffect, useState, useCallback } from 'react';
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';
import { getCookie } from '@/utils/CookieUtil';
import { ViteConfig } from '@/apis/ViteConfig';
import axios from 'axios';
import userStore from '@/stores/useUserStore';
// 타입 정의
interface MatchSocketContextType {
  matchClient: Client | null;
  isMatchConnected: boolean;
  disconnectMatch: () => void;
}

const MatchSocketContext = createContext<MatchSocketContextType | null>(null);

// 프로퍼티 타입 정의
interface MatchSocketProviderProps {
  children: React.ReactNode;
}

export const MatchSocketProvider: React.FC<MatchSocketProviderProps> = ({ children }) => {
  const [stompClient, setStompClient] = useState<Client | null>(null);
  const [isMatchConnected, setIsMatchConnected] = useState(false); // 연결 상태를 추적하는 상태
  const { id } = userStore();
  const BASE_URI = ViteConfig.VITE_BASE_URL;
  const broker = ViteConfig.VITE_SOCK_URL;

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
      webSocketFactory: () => new SockJS(`${BASE_URI}/matching`),
      // debug: str => console.log('STOMP Debug:', str),
      onConnect: () => {
        console.log('Match Client CONNECT');
        setIsMatchConnected(true);
      },
      onDisconnect: async () => {
        console.log('MatchSocket Disconnected');
        setIsMatchConnected(false); // 연결 해제시 상태 업데이트
        /**
         * @description
         * onDisconnect: 연결이 끊어진 경우 호출되는 콜백 함수
         * 1. 서버 또는 네트워크 문제로 인해 연결이 끊어질 때 호출
         * 2. 클라이언트에서 명시적으로 연결을 종료할 때
         * deactivate() 메서드와의 관계
         * 1. deactivate() 메서드를 호출하면 onDisconnect() 콜백 함수가 호출
         */
        // try {
        //   // QUESTION: 자동 매칭이 아닌 다른 곳에서 요청에도 실행된다.
        //   const response = await axios.delete(`${ViteConfig.VITE_BASE_URL}/api/matches/${id}`, {
        //     headers: {
        //       AUTHORIZATION: `Bearer ${token}`,
        //     },
        //   });

        //   console.log(response);
        // } catch (err) {
        //   console.error('Match ID DELETE Request', err);
        // }
      },
      onStompError: frame => {
        console.error('Match Stomp Error:', frame.headers['message']);
        setIsMatchConnected(false); // 에러 발생시 연결 상태 업데이트
      },
      onWebSocketError: err => {
        console.error('MatchSocket Error:', err);
        setIsMatchConnected(false); // 에러 발생시 연결 상태 업데이트
      },
    });

    setStompClient(client);
    client.activate();

    // 클라이언트 비활성화 및 정리
    return () => {
      client.deactivate();
    };
  }, []);

  const disconnectMatch = useCallback(() => {
    if (stompClient && stompClient.connected) {
      stompClient.deactivate();
      setIsMatchConnected(false);
    }
  }, [stompClient]);

  return (
    <MatchSocketContext.Provider
      value={{ matchClient: stompClient, isMatchConnected, disconnectMatch }}>
      {children}
    </MatchSocketContext.Provider>
  );
};

// 컨텍스트 훅
export const useMatchSocket = () => {
  const context = useContext(MatchSocketContext);
  if (!context) {
    throw new Error('useMatchSocket must be used within a MatchSocketProvider');
  }
  return context;
};
