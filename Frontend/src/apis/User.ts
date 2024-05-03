import { ViteConfig } from './ViteConfig';
import jwtAxios from '@/utils/JWTUtil.ts';
import { IAPIResponse } from '@/types/APIResponse.ts';
import { IUser } from '@/types/User.ts';

export const getUserInfo = async (): Promise<IAPIResponse<IUser>> => {
  const res = await jwtAxios.get(`/api/member/me`);
  return res.data;
};
