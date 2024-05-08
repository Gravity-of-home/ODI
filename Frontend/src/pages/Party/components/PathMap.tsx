import { useEffect, useRef } from 'react';

interface IPathMapProps {
  departuresName: string;
  departuresX: number;
  departuresY: number;
  arrivalsName: string;
  arrivalsX: number;
  arrivalsY: number;
  distance: number;
  path: number[][];
}
const PathMap: React.FC<IPathMapProps> = ({
  departuresName,
  arrivalsName,
  departuresX,
  departuresY,
  arrivalsX,
  arrivalsY,
  distance,
  path,
}) => {
  const mapRef = useRef<naver.maps.Map | null>(null);
  const polylineRef = useRef<naver.maps.Polyline | null>(null);
  const markerRef = useRef<naver.maps.Marker | null>(null);

  useEffect(() => {
    //Zoom을 거리에 따라서 적절하게 줘야함
    function calculateZoomLevel(distance: number) {
      if (distance >= 100) {
        return 6;
      } else if (distance >= 50) {
        return 7;
      } else if (distance >= 30) {
        return 8;
      } else if (distance >= 20) {
        return 9;
      } else if (distance >= 10) {
        return 10;
      } else if (distance >= 5) {
        return 11;
      } else if (distance >= 3) {
        return 12;
      } else if (distance >= 1) {
        return 13;
      } else {
        return 14;
      }
    }
    const zoomLevel = calculateZoomLevel(distance / 2.5);
    if (!mapRef.current) {
      mapRef.current = new naver.maps.Map('map', {
        zoom: zoomLevel,
        center: new naver.maps.LatLng((departuresX + arrivalsX) / 2, (departuresY + arrivalsY) / 2),
      });
    }

    if (!polylineRef.current) {
      polylineRef.current = new naver.maps.Polyline({
        path: path.map(coords => new naver.maps.LatLng(coords[1], coords[0])), // 경도, 위도 순서 주의
        strokeColor: '#1463FD',
        strokeOpacity: 1,
        strokeStyle: 'shortdash',
        strokeLineJoin: 'bevel',
        startIcon: naver.maps.PointingIcon.CIRCLE,
        startIconSize: 15,
        endIcon: naver.maps.PointingIcon.BLOCK_ARROW,
        endIconSize: 20,
        strokeWeight: 4,
        map: mapRef.current,
      });
    } else {
      polylineRef.current.setMap(mapRef.current);
    }

    // 이전 지도 요소들을 정리
    return () => {
      if (polylineRef.current) polylineRef.current.setMap(null);
      if (markerRef.current) markerRef.current.setMap(null);
    };
  }, []);

  return (
    <div className='container'>
      <div id='map' className='w-full h-[200px] rounded-xl' />
      <div className='flex'>
        <ul className='steps steps-vertical'>
          <li data-content='●' className='step step-primary'></li>
          <li data-content='▼' className='step step-primary'></li>
        </ul>
        <div>
          <div className='mt-4'>
            <p className='font-bold'>{departuresName}</p>
            <p className=''>출발지</p>
          </div>
          <div className='mt-4'>
            <p className='font-bold'>{arrivalsName}</p>
            <p className=''>도착지</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PathMap;
