// import { create } from 'zustand';
// import { persist, createJSONStorage } from 'zustand/middleware';

// interface ITokenState {
//   token: string;
//   setToken: (newToken: string) => void;
//   getToken: () => string;
//   clearToken: () => void;
// }

// const useAuthStore = create(
//   persist<ITokenState>(
//     (set, get) => ({
//       token: '',
//       setToken: (newToken: string) => set({ token: newToken }),
//       getToken: () => get().token,
//       clearToken: () => set({ token: '' }),
//     }),
//     {
//       name: 'Auth',
//       storage: createJSONStorage(() => sessionStorage),
//     },
//   ),
// );

// export default useAuthStore;
