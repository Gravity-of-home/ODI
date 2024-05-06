import { create } from 'zustand';

interface IMapState {
  googleMap: google.maps.Map | null;
  setGoogleMap: (newMap: google.maps.Map) => void;
  deleteGoogleMap: () => void;
}

const useMapStore = create<IMapState>(set => ({
  googleMap: null,
  setGoogleMap: newMap => set({ googleMap: newMap }),
  deleteGoogleMap: () => set({ googleMap: null }),
}));

export default useMapStore;
