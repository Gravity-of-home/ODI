import axios, {
  AxiosError,
  AxiosInstance,
  AxiosResponse,
  HttpStatusCode,
  InternalAxiosRequestConfig,
} from 'axios';

import { getCookie } from './CookieUtil.ts';
import { ViteConfig } from '@/apis/ViteConfig.ts';

const jwtAxios: AxiosInstance = axios.create();

const isLogin = JSON.parse(localStorage.getItem('User') as string)!.state.isLogin;

export const refreshJWT = async () => {
  const host = ViteConfig.VITE_BASE_URL;

  // const header = { headers: { Authorization: `Bearer ${accessToken}` } };

  // NOTE : 리프레쉬 토큰을 이용하여 새로운 ACCESS TOKEN, REFRESH TOKEN을 발급받는다.

  const res = await axios.post(`${host}/reissue`, {}, { withCredentials: true });
  const Authorization = getCookie('Authorization');

  // NOTE : 결과값은 OK가 오면 정상이다!
  return Authorization;
};

// NOTE : 요청 전 헤더 처리
const beforeReq = async (
  config: InternalAxiosRequestConfig<any>,
): Promise<InternalAxiosRequestConfig<any> | any> => {
  console.log('Before Request...');

  if (!isLogin) {
    console.log('USER NOT FOUND');
    return Promise.reject({ response: { data: { error: 'REQUIRE_LOGIN' } } });
  }

  const accessToken = getCookie('Authorization');
  // const accessToken = await refreshJWT();
  // console.log('refreshJWT RESULT: AccessToken', accessToken);

  config.headers.AUTHORIZATION = `Bearer ${accessToken}`;

  return config;
};

// NOTE : 요청 실패 처리
const requestFail = (err: AxiosError | Error): Promise<AxiosError> => {
  console.log('Request Error...');

  return Promise.reject(err);
};

// NOTE : 요청에 대한 응답 전처리
const beforeRes = async (res: AxiosResponse): Promise<any> => {
  console.log('Before Return Response...');

  const data = res.data;

  // NOTE : 요청 결과를 이용해서 Referesh Token을 재발급 받아야 하는 경우
  // TODO : data.status 가 맞는지 확인하기
  if (data && data.status === HttpStatusCode.BadRequest) {
    const accessToken = await refreshJWT();
    console.log('refreshJWT RESULT : AccessToken =>', accessToken);

    //원래의 호출(정상적인 토큰들로)
    const originalRequest = res.config;

    originalRequest.headers.AUTHORIZATION = `Bearer ${accessToken}`;

    return await axios(originalRequest);
  }

  return res;
};

// NOTE : 응답 실패 처리
const responseFail = (err: AxiosError | Error): Promise<Error> => {
  console.log('Response Fail Error...');

  return Promise.reject(err);
};

jwtAxios.interceptors.request.use(beforeReq, requestFail);

jwtAxios.interceptors.response.use(beforeRes, responseFail);

export default jwtAxios;
