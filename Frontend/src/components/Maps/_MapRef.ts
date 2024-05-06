import { useEffect, useRef, useState } from 'react';
import LatLngAddStore from '@/stores/useLatLngAddStore';
import mapStore from '@/stores/useMapStore';
import DarkModeStyle from './DarkModeStyle';

const MapRef = (() => {
  let googleMap: google.maps.Map;

  const { setGoogleMap } = mapStore();

  const { currentLat, currentLng } = LatLngAddStore();

  const container = document.createElement('div');

  container.id = 'map';
  container.style.width = '100%';
  container.style.height = '100vh';

  document.body.appendChild(container);

  return () => {
    if (!googleMap) {
      googleMap = new window.google.maps.Map(container, {
        center: {
          lat: currentLat,
          lng: currentLng,
        },
        disableDefaultUI: true,
        styles: DarkModeStyle,
        zoom: 16,
        minZoom: 10,
        maxZoom: 18,
        gestureHandling: 'greedy',
        restriction: {
          latLngBounds: {
            north: 43,
            south: 33,
            west: 124,
            east: 132,
          },
          strictBounds: true,
        },
      });
    }

    return setGoogleMap(googleMap);
  };
})();

export default MapRef;
