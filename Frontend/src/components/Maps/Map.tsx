import { Status, Wrapper } from '@googlemaps/react-wrapper';
import MapRef from './MapRef';
import { ViteConfig } from '@/apis/ViteConfig';

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
      return (
        <>
          <MapRef />
        </>
      );
  }
};

const GoogleMap = () => {
  return (
    <Wrapper apiKey={ViteConfig.VITE_GOOGLE_MAP_API_KEY} render={render} libraries={['marker']} />
  );
};

export default GoogleMap;
