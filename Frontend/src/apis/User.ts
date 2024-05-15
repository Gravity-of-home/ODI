import jwtAxios from '@/utils/JWTUtil.ts';
import { IAPIResponse } from '@/types/APIResponse.ts';
import { IUser } from '@/types/User.ts';

export const getUserInfo = async (): Promise<IAPIResponse<IUser>> => {
  const { data } = await jwtAxios.get(`/api/members/me`);
  console.log('USER INFO: ', data.data);
  return data;
};
