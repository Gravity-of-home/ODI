import { Status, Wrapper } from '@googlemaps/react-wrapper';
import MapRef from '@/pages/Home/components/MapRef';
import { ViteConfig } from '@/apis/ViteConfig';

const render = (status: Status) => {
  switch (status) {
    case Status.LOADING:
      return (
        <>
          <div className='w-[100%] h-[100%] flex justify-center items-center'>
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

const GoogleMap = () => {
  return (
    <>
      <Wrapper apiKey={ViteConfig.VITE_GOOGLE_MAP_API_KEY} render={render} libraries={['marker']} />
    </>
  );
};

export default GoogleMap;
