import { create } from 'zustand';

interface IMapState {
  googleMap: google.maps.Map | null;
  departureMarker: google.maps.Marker | null;
  arrivalMarker: google.maps.Marker | null;
  setGoogleMap: (newMap: google.maps.Map) => void;
  setDepartureMarker: (newMarker: google.maps.Marker) => void;
  setArrivalMarker: (newMarker: google.maps.Marker) => void;
  deleteGoogleMap: () => void;
}

const useMapStore = create<IMapState>(set => ({
  googleMap: null,
  departureMarker: null,
  arrivalMarker: null,
  setGoogleMap: newMap => set({ googleMap: newMap }),
  setDepartureMarker: newMarker => set({ departureMarker: newMarker }),
  setArrivalMarker: newMarker => set({ arrivalMarker: newMarker }),
  deleteGoogleMap: () => set({ googleMap: null }),
}));

export default useMapStore;
