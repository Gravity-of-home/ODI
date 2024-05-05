import { Status, Wrapper } from '@googlemaps/react-wrapper';
import { ViteConfig } from '@/apis/ViteConfig';
import MapRef from './MapRef';

const render = (status: Status) => {
  switch (status) {
    case Status.LOADING:
      return (
        <>
          <div className='gird grid-cols-1 gap-6 h-[100%] flex flex-col justify-center items-center animate-fadeIn'>
            <img
              src='https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Travel%20and%20places/Cyclone.png'
              alt='Cyclone'
              width='200'
              height='200'
            />
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
