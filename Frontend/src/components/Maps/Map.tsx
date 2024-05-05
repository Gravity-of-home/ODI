import { Status, Wrapper } from '@googlemaps/react-wrapper';
import { ViteConfig } from '@/apis/ViteConfig';
import MapRef from './MapRef';

const render = (status: Status) => {
  switch (status) {
    case Status.LOADING:
      return (
        <>
          <div className='w-full h-lvh flex justify-center items-center'>
            <span className='loading loading-dots loading-lg'></span>
          </div>
        </>
      );
    case Status.FAILURE:
      return <>에러 발생</>;
    case Status.SUCCESS:
      return (
        <>
          <MapRef />
        </>
      );
  }
};

const Map = () => {
  return <Wrapper apiKey={ViteConfig.VITE_GOOGLE_MAP_API_KEY} render={render} />;
};

export default Map;
