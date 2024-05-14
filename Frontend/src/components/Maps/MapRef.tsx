import { useState, useEffect, useRef } from 'react';
import latLngAddStore from '@/stores/useLatLngAddStore';
import DarkModeStyle from './DarkModeStyle';
import { useNavigate } from 'react-router-dom';
import BottomSheet from '@/components/BottomSheet/BottomSheet';
import partyStore from '@/stores/usePartyStore';
import CURLOCMARKER from '@/assets/image/icons/CURLOCMARKER.png';
import SvgCurLoc from '@/assets/svg/SvgCurLoc.tsx';
import { toast } from 'react-toastify';
import jwtAxios from '@/utils/JWTUtil';
import SvgChat from '@/assets/svg/SvgChat.tsx';
import SvgNotification from '@/assets/svg/SvgNotification';
import SvgProfile from '@/assets/svg/SvgProfile';
import PartyMap from '@/pages/Party/components/PartyMap';
import SvgDepartureMarker from '@/assets/svg/SvgDepartureMarker';
import SvgArrivalMarker from '@/assets/svg/SvgArrivalMarker';
import Front from '@/assets/image/icons/Front.png';
import userStore from '@/stores/useUserStore';
import { getCookie } from '@/utils/CookieUtil';
import { useMatchSocket } from '@/context/matchSocketProvider';

interface IAutoMatchData {
  depName?: string;
  depLon?: number;
  depLat?: number;
  arrName?: string;
  arrLon?: number;
  arrLat?: number;
}

const MapRef = () => {
  const ref = useRef<HTMLDivElement>(null);
  const nav = useNavigate();
  const [map, setMap] = useState<google.maps.Map | null>(null);
  const { currentAdd, currentLat, currentLng } = latLngAddStore();
  const {
    departuresName,
    departuresLocation,
    setDepartures,
    arrivalsName,
    arrivalsLocation,
    setArrivals,
  } = partyStore();
  const [curLocAdd, setCurLocAdd] = useState<string>('내 위치');
  const autoMatchModalRef = useRef<HTMLDialogElement>(null);
  const [isOpen, setIsOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  // NOTE : 자동 매칭 데이터
  const { id } = userStore();
  const [autoMatchData, setAutoMatchData] = useState<IAutoMatchData>({
    depName: '',
    depLon: 0,
    depLat: 0,
    arrName: '',
    arrLon: 0,
    arrLat: 0,
  });
  const [mapCenter, setMapCenter] = useState<google.maps.LatLng>(
    new google.maps.LatLng({ lat: currentLat, lng: currentLng }),
  );
  const [curMarker, setCurMarker] = useState<google.maps.Marker | null>(null);
  const [markers, setMarkers] = useState<google.maps.Marker[]>([]);
  const { matchClient, isMatchConnected, disconnectMatch } = useMatchSocket();

  const goCreateParty = () => {
    nav('/party');
  };

  const openAutoMatchModal = () => {
    if (autoMatchModalRef.current) {
      setIsOpen(true);
      autoMatchModalRef.current.showModal();
    }
  };

  // TODO : 자동 매칭 이후 사용
  const closeAutoMatchModal = () => {
    if (autoMatchModalRef.current) {
      setIsOpen(false);
      autoMatchModalRef.current.close();
    }
  };

  // TODO  : 자동 매칭 신청 함수
  const reqAutoMatch = () => {
    setIsLoading(true);
    const data = {
      depName: departuresName,
      depLon: departuresLocation!.longitude,
      depLat: departuresLocation!.latitude,
      arrName: arrivalsName,
      arrLon: arrivalsLocation!.longitude,
      arrLat: arrivalsLocation!.latitude,
    };
    setAutoMatchData(data);
    try {
      if (matchClient && matchClient.connected) {
        console.log(data);
        matchClient.publish({
          destination: `/pub/match/${id}`,
          body: JSON.stringify(data),
          headers: {
            token: `${getCookie('Authorization')}`,
          },
        });
      }
      // setIsLoading(false);
      setDepartures?.('내 위치', { latitude: currentLat, longitude: currentLng });
      setArrivals?.('도착지를 설정해 주세요.', { latitude: 0, longitude: 0 });
      // closeAutoMatchModal();
    } catch (error) {}
  };

  const goSetDeparture = () => {
    nav('/party/departure');
  };

  const goSetArrival = () => {
    nav('/party/arrival');
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

  useEffect(() => {
    if (matchClient && matchClient.connected) {
      const subscription = matchClient.subscribe(
        `/sub/matchResult/${id}`,
        message => {
          console.log(JSON.parse(message.body));
          const newMessage = JSON.parse(message.body);
          console.log(newMessage.type);

          switch (newMessage.type) {
            case 'MATCH_SUCCESS':
              console.log(newMessage);
              setIsLoading(false);
              break;
            case 'MATCH_NOT_FOUND':
              // 아무것도 안온다 찾는중 띄우기 로..딩~
              break;
            case 'ALREADY_REQUEST':
              // 아무것도 안온다. 이미 요청했을때 온다.
              // 1. 브라우저 다르게 들어가면, 아이디 지우기 보내기
              // 2. 이미 요청했다고 알림창 띄우기
              // 매칭 페이지 끄기
              closeAutoMatchModal();
              break;
          }
        },
        {
          token: `${getCookie('Authorization')}`,
        },
      );

      return () => subscription.unsubscribe();
    }
  }, [matchClient, isMatchConnected]);

  let autoMatchModal = (
    <>
      <dialog
        ref={autoMatchModalRef}
        id='my_modal_4'
        className={`modal ${isOpen ? 'open' : 'close'}`}>
        <div className='modal-box w-11/12 h-[60%] bg-black'>
          <h3 className='font-bold text-white text-[20px]'>자동 매칭</h3>
          <div className='mt-1 border border-gray-500'></div>
          {isLoading === false ? (
            <>
              <div className='h-[40%] mx-8 mt-5 border border-gray-500 rounded-xl flex flex-col items-center justify-center overflow-hidden'>
                <PartyMap />
              </div>
              <div className='h-[30%] mx-8 mt-5'>
                <div className='w-[100%] h-[50%] flex justify-between'>
                  <div className='w-[8%] h-[100%]'>
                    <SvgDepartureMarker width={'100%'} height={'100%'} />
                  </div>
                  <div className='w-[85%]' onClick={goSetDeparture}>
                    <div className='flex justify-between'>
                      <div className='font-bold mb-1 text-[17px]'>
                        {!!departuresName ? departuresName : currentAdd}
                      </div>
                      <div className='w-[10%] flex justify-center items-center'>
                        <img src={Front} alt='출발지 설정' />
                      </div>
                    </div>
                    <div className='text-gray-500'>출발지</div>
                  </div>
                </div>
                <div className='w-[100%] h-[50%] flex justify-between'>
                  <div className='w-[8%] h-[100%]'>
                    <SvgArrivalMarker width={'100%'} height={'35px'} />
                  </div>
                  <div className='w-[85%]' onClick={goSetArrival}>
                    <div className='flex justify-between'>
                      <div className='font-bold mb-1 text-[17px]'>{arrivalsName}</div>
                      <div className='w-[10%] flex justify-center items-center'>
                        <img src={Front} alt='도착지 설정' />
                      </div>
                    </div>
                    <div className='text-gray-500'>도착지</div>
                  </div>
                </div>
              </div>
            </>
          ) : (
            <>
              <div className='w-[100%] h-[100%] flex justify-center items-center'>
                <span className='loading loading-dots loading-lg'></span>
              </div>
            </>
          )}

          <div className='modal-action'>
            <form method='dialog'>
              <button className='btn btn-sm btn-circle btn-ghost absolute right-5 top-5'>✕</button>
            </form>
          </div>
          <button className='btn bg-OD_PURPLE text-white' onClick={reqAutoMatch}>
            설정하기
          </button>
        </div>
      </dialog>
    </>
  );

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
      {'자동 매칭 모달' && autoMatchModal}
      <button
        className='absolute btn z-10 w-[15%] h-[5%] bottom-[20%] right-[3%] bg-black text-white hover:text-OD_GREEN'
        onClick={openAutoMatchModal}>
        자동 매칭
      </button>
      <BottomSheet />
    </div>
  );
};

export default MapRef;
