/**
 * @description 쿠키를 다루는 함수들을 정의한 파일
 * NOTE: 쿠키는 클라이언트에 저장되는 정보로, 서버에서는 사용할 수 없다.
 * NOTE: 리프레쉬 토큰과 기간을 맞춰서 쿠키를 설정한다.
 */

import { Cookies } from 'react-cookie';

const cookies: Cookies = new Cookies();

// NOTE: 쿠키를 설정하는 함수 (소셜 로그인 과정에서 받아와서 사용하지만, 상호보완을 위해서 만든 함수)
export const setCookie = (name: string, value: string, days: number) => {
  const expires = new Date();
  expires.setUTCDate(expires.getUTCDate() + days);
  // NOTE: path: '/'는 모든 경로에서 쿠키를 사용할 수 있도록 한다.
  return cookies.set(name, value, { path: '/', expires: expires });
};

export const getCookie = (name: string) => {
  return cookies.get(name);
};

export const removeCookie = (name: string, path: string = '/') => {
  cookies.remove(name, { path });
};
