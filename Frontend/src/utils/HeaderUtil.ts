import jwtAxios from './JWTUtil.ts';

function setHeader(key: string, value: string) {
  jwtAxios.defaults.headers.common[key] = value;
}

function removeHeader(key: string) {
  if (!jwtAxios.defaults.headers.common[key]) {
    return;
  }

  delete jwtAxios.defaults.headers.common[key];
}

export { setHeader, removeHeader };
