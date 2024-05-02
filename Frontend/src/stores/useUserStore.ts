import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import { IUser } from '../types/User.ts';
interface IUserState extends IUser {
  isLogin: boolean;
  Login: () => void;
  Logout: () => void;
  loginUser: (user: IUser) => void;
  logoutUser: () => void;
}

const useUserStore = create(
  persist<IUserState>(
    set => ({
      isLogin: false,
      memberId: '',
      name: '',
      gender: false,
      email: '',
      birth: new Date(),
      nickname: '',
      point: 0,
      profileImg: '',
      isVerified: false,
      brix: 0,
      Login: () => set({ isLogin: true }),
      Logout: () => set({ isLogin: false }),
      loginUser: ({
        memberId,
        name,
        gender,
        email,
        birth,
        nickname,
        point,
        profileImg,
        isVerified,
        brix,
      }) =>
        set({
          memberId: memberId,
          name: name,
          gender: gender,
          email: email,
          birth: birth,
          nickname: nickname,
          point: point,
          profileImg: profileImg,
          isVerified: isVerified,
          brix: brix,
        }),
      logoutUser: () => {
        set({
          memberId: '',
          name: '',
          gender: false,
          email: '',
          birth: new Date(),
          nickname: '',
          point: 0,
          profileImg: '',
          isVerified: false,
          brix: 0,
        });
      },
    }),
    {
      name: 'User',
    },
  ),
);

export default useUserStore;
