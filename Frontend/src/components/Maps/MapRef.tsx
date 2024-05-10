import { useState, useEffect, useRef } from 'react';
import LatLngAddStore from '@/stores/useLatLngAddStore';
import DarkModeStyle from './DarkModeStyle';
import { Layout } from '../Layout';
import { useNavigate } from 'react-router-dom';
import { Category, categoryIcons } from '@/constants/constants';

const MapRef = () => {
  const ref = useRef<HTMLDivElement>(null);
  const nav = useNavigate();
  const [map, setMap] = useState<google.maps.Map | null>(null);
  const { currentLat, currentLng } = LatLngAddStore();
  const [mapCenter, setMapCenter] = useState<google.maps.LatLng>(
    new google.maps.LatLng({ lat: currentLat, lng: currentLng }),
  );
  const [markers, setMarkers] = useState<google.maps.Marker[]>([]);

  const goCreateParty = () => {
    nav('/party-boards');
  };

  useEffect(() => {
    if (ref.current && !map) {
      const initialMap = new google.maps.Map(ref.current, {
        center: mapCenter,
        disableDefaultUI: true,
        clickableIcons: false,
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
      setMap(initialMap);
    }
  }, [ref, mapCenter, map]);

  // NOTE : 서버 데이터로부터 마커 생성하기
  // useEffect(() => {
  //   if (map) {
  //     fetch('YOUR_API_ENDPOINT').then(response => response.json()).then(dataList => {
  //       updateMarkers(dataList);
  //     });
  //   }
  // }, [map, mapCenter]);

  // const updateMarkers = (dataList:타입[]) => {
  //   // Clear existing markers
  //   markers.forEach(marker => marker.setMap(null));
  //   const newMarkers = dataList.map(item => {
  //     const marker = new google.maps.Marker({
  //       position: new google.maps.LatLng(item.latitude, item.longitude),
  //       map,
  //       icon: {
  //         url: categoryIcons[item.category],
  //         scaledSize: new google.maps.Size(35, 35)
  //       },
  //     });

  //     // Add click listener to log data
  //     marker.addListener('click', () => {
  //       console.log(item);
  //     });

  //     return marker;
  //   });

  //   setMarkers(newMarkers);
  //   adjustMapBounds(newMarkers);
  // };

  // const adjustMapBounds = (newMarkers:google.maps.Marker[]) => {
  //   const bounds = new google.maps.LatLngBounds();
  //   newMarkers.forEach(marker => bounds.extend(marker.getPosition() as google.maps.LatLng));
  //   map!.fitBounds(bounds);
  // };

  return (
    <Layout>
      <div ref={ref} id='map' className='w-[100%] h-[100%]' />
      <button
        className='btn z-10 w-[15%] h-[5%] absolute right-0 bottom-[10%]'
        onClick={goCreateParty}>
        글 생성 가보자!
      </button>
    </Layout>
  );
};

export default MapRef;
