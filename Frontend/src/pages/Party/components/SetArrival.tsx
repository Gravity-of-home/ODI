import { Status, Wrapper } from '@googlemaps/react-wrapper';
import { ViteConfig } from '@/apis/ViteConfig';
import { useState, useEffect, useRef, ChangeEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import partyStore from '@/stores/usePartyStore';
import LatLngAddStore from '@/stores/useLatLngAddStore';
import DarkModeStyle from '@/components/Maps/DarkModeStyle';
import ARRIVAL from '@/assets/image/icons/SETARRIVAL.png';
import SvgSearch from '@/assets/svg/SvgSearch';
import SvgGoTo from '@/assets/svg/SvgGoTo';
import SvgGoBack from '@/assets/svg/SvgGoBack';
import SvgCurLoc from '@/assets/svg/SvgCurLoc';
import SvgArrivalMarker from '@/assets/svg/SvgArrivalMarker';
import { IPlaceInfo } from '@/types/Party';
import jwtAxios from '@/utils/JWTUtil';
import SearchArrivalItem from './SearchArrivalItem';
import { toast } from 'react-toastify';

const MapRef = () => {
  const ref = useRef<HTMLDivElement>(null);
  const nav = useNavigate();
  const { currentLat, currentLng } = LatLngAddStore();
  const { setArrivals, arrivalsName, departuresLocation, arrivalsLocation } = partyStore();
  const [map, setMap] = useState<google.maps.Map | null>(null);
  const [marker, setMarker] = useState<google.maps.Marker | null>(null);
  const [mapCenter, setMapCenter] = useState<google.maps.LatLng>(
    new google.maps.LatLng({ lat: arrivalsLocation!.latitude, lng: arrivalsLocation!.longitude }),
  );
  const [arrName, setArrName] = useState<string>(arrivalsName as string);

  const successReq = () => {
    return toast.success('도착지가 설정되었습니다.');
  };
  const failReq = () => {
    return toast.error('도착지 설정에 실패했습니다.');
  };

  const goCurrentLoc = () => {
    navigator.geolocation.getCurrentPosition(
      getPosSuccess,
      () => alert('위치 정보를 가져오는데 실패했습니다.'),
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

    if (map && marker) {
      map!.setZoom(17);
      // 지도를 이동 시킨다.
      map!.panTo(currentPos as google.maps.LatLng);

      // 마커를 이동 시킨다.
      marker!.setPosition(currentPos);
    }
  };

  // const getAddByGeoCode = (lat: number, lng: number) => {
  //   const geocoder = new google.maps.Geocoder();
  //   const latlng = new google.maps.LatLng(lat, lng);

  //   geocoder.geocode({ location: latlng }, (results, status) => {
  //     if (status === 'OK') {
  //       console.log(results);
  //     }
  //   });
  // };

  const getAddByLatLng = async (lat: number, lng: number) => {
    try {
      const res = await jwtAxios.get(
        `${ViteConfig.VITE_BASE_URL}/api/places/place?longitude=${lng}&latitude=${lat}`,
      );

      console.log(res);
      console.log('장소 이름 : ', res.data.data.placeName);
      console.log('건물 이름 : ', res.data.data.buildingName);
      console.log('지번 주소 : ', res.data.data.jibunAddress);
      console.log('도로명 주소 : ', res.data.data.roadNameAddress);
      setArrName(
        res.data.data.placeName === null ? res.data.data.buildingName : res.data.data.placeName,
      );
      setArrivals?.(
        res.data.data.placeName === null ? res.data.data.buildingName : res.data.data.placeName,
        { longitude: lng, latitude: lat },
      );
      successReq();
    } catch (error) {
      failReq();
    }
  };

  console.log(arrivalsName, arrivalsLocation);

  useEffect(() => {
    if (ref.current) {
      const initialMap = new window.google.maps.Map(ref.current, {
        center: mapCenter,
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

      setMap(initialMap);

      setMapCenter(initialMap.getCenter() as google.maps.LatLng);

      const markerInstance = new window.google.maps.Marker({
        position: initialMap.getCenter(),
        map: initialMap,
        icon: {
          url: ARRIVAL,
          scaledSize: new window.google.maps.Size(32, 42),
        },
        draggable: true,
        zIndex: 10,
        animation: google.maps.Animation.DROP,
      });

      setMarker(markerInstance);

      markerInstance.addListener('dragstart', () => {
        markerInstance.setAnimation(google.maps.Animation.BOUNCE);
      });

      markerInstance.addListener('dragend', () => {
        const markerCurPos = markerInstance.getPosition();
        console.log(markerCurPos?.lat(), markerCurPos?.lng());

        markerInstance.setAnimation(null);
        getAddByLatLng(markerCurPos!.lat(), markerCurPos!.lng());
        initialMap.panTo(markerCurPos as google.maps.LatLng);
      });

      // markerInstance.addListener('click', () => {});

      markerInstance.setMap(initialMap);
    }
  }, [mapCenter]);

  return (
    <>
      <div
        className='fixed w-[50px] h-[50px] bottom-[30%] right-[3%] z-10 flex flex-col justify-center items-center bg-black rounded-full cursor-pointer'
        onClick={goCurrentLoc}>
        <SvgCurLoc style={{ display: 'flex' }} />
      </div>
      <div ref={ref} id='map' className='top-[5%] w-[100%] h-[75%]' />
      <div className='fixed w-[100%] h-[25%] z-10 bg-black rounded-t-2xl px-5 py-5'>
        <div className='w-[100%] h-[15%] text-[18px] text-white font-semibold flex items-center'>
          도착지를 설정해주세요
        </div>
        <div className='w-[100%] h-[15%] font-semibold flex items-center my-5'>
          <SvgArrivalMarker width={'10%'} height={'100%'} />
          <div className='w-[90%] h-[100%] pl-2 flex items-center'>{arrName}</div>
        </div>
        <div className='w-[100%] h-[40%] flex justify-center items-center'>
          <div
            className='w-[80%] h-[70%] bg-OD_PURPLE flex justify-center items-center font-semibold text-[20px] text-white rounded-xl hover:bg-OD_GREEN hover:text-black'
            onClick={() => {
              nav('/party-boards', { replace: true });
            }}>
            설정완료
          </div>
        </div>
      </div>
    </>
  );
};

const render = (status: Status) => {
  switch (status) {
    case Status.LOADING:
      return (
        <>
          <span className='loading loading-dots loading-lg'></span>
        </>
      );
    case Status.FAILURE:
      return <>에러 발생</>;
    case Status.SUCCESS:
      return <MapRef />;
  }
};

const SetArrival = () => {
  const [isSearch, setIsSearch] = useState<boolean>(true);
  const [search, setSearch] = useState<string>('');
  const [searchData, setSearchData] = useState<IPlaceInfo[]>([]);
  const { departuresLocation } = partyStore();
  const searchRef = useRef<HTMLInputElement>(null);
  const nav = useNavigate();

  const handleSearch = () => {
    setIsSearch(true);
    if (searchRef.current) {
      searchRef.current.focus();
    }
  };

  const failReq = () => {
    setSearchData([]);
    return toast.error('검색 결과가 없습니다.');
  };

  const getSearchData = async (searchValue: string) => {
    if (searchValue === '') {
      setSearchData([]);
      return;
    }
    try {
      const res = await jwtAxios.get(
        `${ViteConfig.VITE_BASE_URL}/api/places?query=${searchValue}&latitude=${departuresLocation!.latitude}&longitude=${departuresLocation!.longitude}`,
      );
      console.log(res.data.data.content);
      setSearchData(res.data.data.content);
    } catch (error) {
      failReq();
    }
    // console.log(res);
  };

  const getLocationBySearch = (e: ChangeEvent<HTMLInputElement>) => {
    setSearch(e.target.value);
    getSearchData(e.target.value);
  };

  return (
    <>
      <div className='fixed w-[100%] h-[5%] bg-black z-10 flex items-center'>
        <div
          className='px-4 z-10'
          onClick={() => {
            nav(-1);
          }}>
          <SvgGoBack />
        </div>
        <div className='fixed w-[100%] flex justify-center text-[18px] font-semibold text-white'>
          도착지
        </div>
      </div>
      <div
        className='fixed w-[100%] h-[10%] top-[5%] z-10 flex justify-center items-center'
        onClick={handleSearch}>
        <div className='absolute w-[90%] h-[60%] flex justify-between items-center px-5 py-2 z-10'>
          <SvgSearch />
          <SvgGoTo />
        </div>
        <input
          type='text'
          ref={searchRef}
          value={search}
          className='w-[90%] h-[60%] flex items-center bg-black rounded-xl px-12 py-2'
          placeholder='어디로 가고싶으신가요?'
          onChange={getLocationBySearch}
        />
      </div>
      {isSearch === false ? (
        <Wrapper
          apiKey={ViteConfig.VITE_GOOGLE_MAP_API_KEY}
          render={render}
          libraries={['marker']}
        />
      ) : (
        <div className='fixed top-[15%] w-[100%] h-[85%]'>
          {searchData.length !== 0 ? (
            <div className='w-[100%] h-[100%] overflow-y-auto'>
              {searchData.map(data => (
                <SearchArrivalItem key={data.id} {...data} setIsSearch={setIsSearch} />
              ))}
            </div>
          ) : (
            <div className='w-[100%] h-[100%] flex justify-center items-center font-semibold text-[18px]'>
              검색 결과가 없습니다.
            </div>
          )}
        </div>
      )}
    </>
  );
};

export default SetArrival;
