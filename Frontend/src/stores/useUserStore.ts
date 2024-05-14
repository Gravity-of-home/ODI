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
      id: 0,
      name: '',
      gender: '',
      email: '',
      birth: new Date(),
      ageGroup: '',
      nickname: '',
      point: 0,
      image: '',
      isVerified: false,
      brix: 0,
      Login: () => set({ isLogin: true }),
      Logout: () => set({ isLogin: false }),
      loginUser: ({
        id,
        name,
        gender,
        email,
        birth,
        ageGroup,
        nickname,
        point,
        image,
        isVerified,
        brix,
      }) =>
        set({
          id: id,
          name: name,
          gender: gender,
          email: email,
          birth: birth,
          ageGroup: ageGroup,
          nickname: nickname,
          point: point,
          image: image,
          isVerified: isVerified,
          brix: brix,
        }),
      logoutUser: () => {
        set({
          id: 0,
          name: '',
          gender: '',
          email: '',
          birth: new Date(),
          ageGroup: '',
          nickname: '',
          point: 0,
          image: '',
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
