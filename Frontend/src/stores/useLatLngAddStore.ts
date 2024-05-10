import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface ILatLngAddState {
  currentLat: number;
  currentLng: number;
  currentAdd: string;
  setLatLngAdd: (currentLat: number, currentLng: number, currentAdd?: string) => void;
  removeAll: () => void;
}

const useLatLngAddStore = create(
  persist<ILatLngAddState>(
    set => ({
      currentLat: 36.1071359,
      currentLng: 128.4161679,
      currentAdd: 'DEFAULT',
      setLatLngAdd: (Lat, Lng, currentAdd) =>
        set(() => ({ currentLat: Lat, currentLng: Lng, currentAdd: currentAdd })),
      removeAll: () => set(() => ({ currentLat: 0, currentLng: 0, currentAdd: '' })),
    }),
    {
      name: 'LatLngAdd',
      storage: {
        getItem: name => {
          const item = sessionStorage.getItem(name);
          return item ? JSON.parse(item) : null;
        },
        setItem: (name, value) => {
          const stringValue = JSON.stringify(value);
          sessionStorage.setItem(name, stringValue);
        },
        removeItem: name => sessionStorage.removeItem(name),
      },
    },
  ),
);

export default useLatLngAddStore;
