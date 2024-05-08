import { ViteConfig } from '@/apis/ViteConfig';
import { useState } from 'react';
import { Map, Marker, APIProvider } from '@vis.gl/react-google-maps';
import DarkModeStyle from '@/components/Maps/DarkModeStyle';
import partyStore from '@/stores/usePartyStore';
import departureMarker from '@/assets/image/icons/departureMarker.png';

const SetDeparture = () => {
  const { departuresLocation } = partyStore();
  const [isLoaded, setIsLoaded] = useState(false);

  return (
    <APIProvider
      apiKey={ViteConfig.VITE_GOOGLE_MAP_API_KEY}
      onLoad={() => {
        setIsLoaded(true);
      }}>
      {isLoaded === true ? (
        <Map
          styles={DarkModeStyle}
          style={{ width: '100%', height: '100%' }}
          clickableIcons={false}
          disableDefaultUI={true}
          defaultCenter={{ lat: departuresLocation!.latitude, lng: departuresLocation!.longitude }}
          defaultZoom={16}
          minZoom={10}
          maxZoom={18}
          restriction={{
            latLngBounds: {
              north: 43,
              south: 33,
              west: 124,
              east: 132,
            },
            strictBounds: true,
          }}>
          <Marker
            position={{ lat: departuresLocation!.latitude, lng: departuresLocation!.longitude }}
            icon={departureMarker}
            zIndex={10}></Marker>
        </Map>
      ) : (
        <>
          <span className='loading loading-dots loading-lg'></span>
        </>
      )}
    </APIProvider>
  );
};

export default SetDeparture;
