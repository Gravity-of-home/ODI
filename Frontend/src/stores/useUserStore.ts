import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface IUserState {
  isLogin: boolean;
  id: number;
  name?: string;
  email?: string;
  phone?: string;
  accessToken: string;
  refreshToken: string;
  profile?: string;
  manager?: string;
  address?: string;
  registrationNumber?: string;
  registrationFile?: string;
  setTokens: (accessToken: string) => void;
  loginUser: ({
    id,
    name,
    email,
    phone,
    profile,
    accessToken,
    refreshToken,
  }: {
    id: number;
    name?: string;
    email?: string;
    phone?: string;
    profile?: string;
    accessToken: string;
    refreshToken: string;
  }) => void;
  logoutUser: () => void;
}

const useUserStore = create(
  persist<IUserState>(
    set => ({
      isLogin: false,
      id: 0,
      name: '',
      email: '',
      phone: '',
      accessToken: '',
      refreshToken: '',
      profile: undefined,
      manager: undefined,
      address: undefined,
      registrationNumber: undefined,
      registrationFile: undefined,
      setTokens: accessToken => set(() => ({ accessToken })),
      loginUser: ({ id, name, email, phone, profile, accessToken, refreshToken }) =>
        set({
          id: id,
          name: name,
          email: email,
          phone: phone,
          profile: profile,
          accessToken: accessToken,
          refreshToken: refreshToken,
          isLogin: true,
        }),
      logoutUser: () => {
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        set({
          isLogin: false,
          id: 0,
          name: '',
          email: '',
          phone: '',
          accessToken: '',
          refreshToken: '',
          profile: undefined,
          manager: undefined,
          address: undefined,
          registrationNumber: undefined,
          registrationFile: undefined,
        });
      },
    }),
    {
      name: 'user-store',
    },
  ),
);

export default useUserStore;
