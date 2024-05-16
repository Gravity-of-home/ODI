import { create } from 'zustand';

interface IMapState {
  googleMap?: google.maps.Map | null;
  latitude?: number;
  longitude?: number;
  setGoogleMap?: (newMap: google.maps.Map) => void;
  setLatitude?: (newLatitude: number) => void;
  setLongitude?: (newLongitude: number) => void;
}

const useMapStore = create<IMapState>(set => ({
  googleMap: null,
  latitude: 0,
  longitude: 0,
  setGoogleMap: newMap => set({ googleMap: newMap }),
  setLatitude: newLatitude => set({ latitude: newLatitude }),
  setLongitude: newLongitude => set({ longitude: newLongitude }),
}));

export default useMapStore;
