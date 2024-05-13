import { useState, useEffect, useRef } from 'react';
import LatLngAddStore from '@/stores/useLatLngAddStore';
import DarkModeStyle from './DarkModeStyle';
import { useNavigate } from 'react-router-dom';
import { Category, categoryIcons } from '@/constants/constants';
import SvgGoBack from '@/assets/svg/SvgGoBack';
import BottomSheet from '@/components/BottomSheet/BottomSheet';
import partyStore from '@/stores/usePartyStore';
import CURLOCMARKER from '@/assets/image/icons/CURLOCMARKER.png';
import SvgCurLoc from '@/assets/svg/SvgCurLoc.tsx';
import { toast } from 'react-toastify';
import jwtAxios from '@/utils/JWTUtil';
import SvgChat from '@/assets/svg/SvgChat.tsx';
import SvgNotification from '@/assets/svg/SvgNotification';
import SvgProfile from '@/assets/svg/SvgProfile';

const MapRef = () => {
  const ref = useRef<HTMLDivElement>(null);
  const nav = useNavigate();
  const [map, setMap] = useState<google.maps.Map | null>(null);
  const { currentLat, currentLng } = LatLngAddStore();
  const { setDepartures } = partyStore();
  const [curLocAdd, setCurLocAdd] = useState<string>('내 위치');
  const [mapCenter, setMapCenter] = useState<google.maps.LatLng>(
    new google.maps.LatLng({ lat: currentLat, lng: currentLng }),
  );
  const [curMarker, setCurMarker] = useState<google.maps.Marker | null>(null);
  const [markers, setMarkers] = useState<google.maps.Marker[]>([]);

  const goCreateParty = () => {
    nav('/party-boards');
  };

  const successReq = () => {
    console.log('현위치 조회에 성공했습니다.');
  };
  const failReq = () => {
    return toast.error('현위치 조회에 실패했습니다.');
  };

  const goCurrentLoc = () => {
    navigator.geolocation.getCurrentPosition(
      getPosSuccess,
      () => toast.error('위치 정보를 가져오는데 실패했습니다.'),
      {
        enableHighAccuracy: true,
        maximumAge: 30000,
        timeout: 27000,
      },
    );
  };

  const getPosSuccess = (pos: GeolocationPosition) => {
    // 현재 위치(위도, 경도) 가져온다.

    const currentPos = new google.maps.LatLng({
      lat: pos.coords.latitude,
      lng: pos.coords.longitude,
    });

    setDepartures?.('내 위치', { latitude: pos.coords.latitude, longitude: pos.coords.longitude });

    if (map && curMarker) {
      map!.setZoom(16);
      // 지도를 이동 시킨다.
      map!.panTo(currentPos as google.maps.LatLng);

      // 마커를 이동 시킨다.
      curMarker!.setPosition(currentPos);
    }
  };

  const getAddByLatLng = async (lat: number, lng: number) => {
    try {
      const res = await jwtAxios.get(`/api/places/place?longitude=${lng}&latitude=${lat}`);

      console.log(res);
      console.log('장소 이름 : ', res.data.data.placeName);
      console.log('건물 이름 : ', res.data.data.buildingName);
      console.log('지번 주소 : ', res.data.data.jibunAddress);
      console.log('도로명 주소 : ', res.data.data.roadNameAddress);
      setCurLocAdd(
        res.data.data.roadNameAddress !== null
          ? res.data.data.roadNameAddress
          : res.data.data.jibunAddress,
      );
      successReq();
    } catch (error) {
      failReq();
    }
  };

  useEffect(() => {
    getAddByLatLng(currentLat, currentLng);
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

      setMapCenter(initialMap.getCenter() as google.maps.LatLng);

      const markerInstance = new window.google.maps.Marker({
        position: initialMap.getCenter(),
        map: initialMap,
        icon: {
          url: CURLOCMARKER,
          scaledSize: new window.google.maps.Size(50, 50),
        },
        draggable: false,
        zIndex: 10,
      });

      setCurMarker(markerInstance);
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
    <div className='w-[100%] h-[100%]'>
      <div className='fixed w-[100%] h-[5%] bg-black z-10 flex items-center'>
        <div className='fixed w-[100%] flex pl-3 text-[18px] font-semibold text-white'>
          {curLocAdd}
          <div
            className='flex justify-center items-center px-2'
            onClick={() => {
              console.log('주소 변경 클릭~!');
            }}>
            <div className='border border-slate-500 rounded-full px-2 text-[12px]'>변경</div>
          </div>
        </div>
        <div className='fixed w-[100%] flex justify-end px-3'>
          <div className='px-2 z-10' onClick={() => {}}>
            <SvgChat />
          </div>
          <div className='px-2 z-10' onClick={() => {}}>
            <SvgNotification />
          </div>
          <div
            className='px-2 z-10'
            onClick={() => {
              nav('/profile');
            }}>
            <SvgProfile />
          </div>
        </div>
      </div>
      <div
        className='fixed w-[50px] h-[50px] bottom-[30%] right-[3%] z-10 flex flex-col justify-center items-center bg-black rounded-full cursor-pointer'
        onClick={goCurrentLoc}>
        <SvgCurLoc style={{ display: 'flex' }} />
      </div>
      <div ref={ref} id='map' className='w-[100%] h-[100%]' />
      <button
        className='absolute btn z-10 w-[15%] h-[5%] bottom-[13%] right-[3%] bg-black text-white hover:text-OD_GREEN'
        onClick={goCreateParty}>
        파티 생성
      </button>
      <button
        className='absolute btn z-10 w-[15%] h-[5%] bottom-[20%] right-[3%] bg-black text-white hover:text-OD_GREEN'
        onClick={() => {
          console.log('자동 매칭');
        }}>
        자동 매칭
      </button>
      <BottomSheet />
    </div>
  );
};

export default MapRef;
