import { ViteConfig } from './ViteConfig';
import jwtAxios from '@/utils/JWTUtil.ts';
import { IAPIResponse } from '@/types/APIResponse.ts';
import { IUser } from '@/types/User.ts';
import userStore from '@/stores/useUserStore';

const { logoutUser, Logout } = userStore();
export const getUserInfo = async (userId: string): Promise<IAPIResponse<IUser>> => {
  const res = await jwtAxios.get(`${ViteConfig.VITE_BASE_URL}/api/member/${userId}`);
  return res.data;
};
