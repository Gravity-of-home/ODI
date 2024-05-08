import { APIProvider, Map } from '@vis.gl/react-google-maps';
import { ViteConfig } from '@/apis/ViteConfig';
import { useState, useEffect } from 'react';
import LatLngAddStore from '@/stores/useLatLngAddStore';
import DarkModeStyle from './DarkModeStyle';

const GoogleMap = () => {
  const VITE_GOOGLE_MAP_API_KEY = ViteConfig.VITE_GOOGLE_MAP_API_KEY;
  const { currentLat, currentLng } = LatLngAddStore();
  const [isLoaded, setIsLoaded] = useState(false);

  // TODO : SearchBar 구현 -> 엘라스틱 서치를 적용
  // 엘라스틱 서치 결과를 바탕으로 GET 요청을 보내서 마커를 찍어야함 & Bottom Sheet 구현

  return (
    <APIProvider
      apiKey={VITE_GOOGLE_MAP_API_KEY}
      onLoad={() => {
        setIsLoaded(true);
      }}>
      {isLoaded === true ? (
        <Map
          styles={DarkModeStyle}
          style={{ width: '100%', height: '100%' }}
          disableDefaultUI={true}
          defaultCenter={{ lat: currentLat, lng: currentLng }}
          defaultZoom={16}
          minZoom={10}
          maxZoom={18}
          gestureHandling={'greedy'}
          restriction={{
            latLngBounds: {
              north: 43,
              south: 33,
              west: 124,
              east: 132,
            },
            strictBounds: true,
          }}
        />
      ) : (
        <>
          <span className='loading loading-dots loading-lg'></span>
        </>
      )}
    </APIProvider>
  );
};

export default GoogleMap;
