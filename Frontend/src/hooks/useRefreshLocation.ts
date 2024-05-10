import LatLngAddstore from '@/stores/useLatLngAddStore';
import usePartyStore from '@/stores/usePartyStore';
// import { distance } from '../Pages/util/calc';

interface PositionOptions {
  coords: {
    latitude: number;
    longitude: number;
  };
}

interface Options {
  enableHighAccuracy: boolean;
  timeout: number;
  maximumAge: number;
}

function watchPositionHook() {
  // const { currentLat, currentLng } = LatLngAddstore((state) => state);

  async function success(pos: PositionOptions) {
    // let d = 0;
    // if (currentLat !== 0 && currentLng !== 0) {
    //     d = distance(currentLat, currentLng, pos.coords.latitude, pos.coords.longitude);
    // }
    // console.log(d);
    // if (d < 10) {
    // localStorage.setItem('latitude', pos.coords.latitude.toString());
    // localStorage.setItem('longitude', pos.coords.longitude.toString());

    LatLngAddstore.setState({
      currentAdd: '내 위치',
      currentLat: pos.coords.latitude,
      currentLng: pos.coords.longitude,
    });
    // usePartyStore.setState({
    //   departuresName: '내 위치',
    //   departuresLocation: { longitude: pos.coords.longitude, latitude: pos.coords.latitude },
    //   arrivalsLocation: { longitude: pos.coords.longitude, latitude: pos.coords.latitude },
    // });

    // }
  }

  function error(err: { code: number; message: string }) {
    alert('ERROR(' + err.code + '): ' + err.message);
  }

  const options: Options | undefined = {
    enableHighAccuracy: true,
    timeout: Infinity,
    maximumAge: 0,
  };
  if (navigator.geolocation) {
    navigator.geolocation.watchPosition(success, error, options);
  } else {
    alert('위치정보 사용 불가능');
  }
}

export default watchPositionHook;
