// import getUserInfoQuery
import { useEffect } from 'react';
import Map from '@/components/Maps/Map';
import { Layout } from '@/components/Layout';
import watchPositionHook from '@/hooks/useRefreshLocation';

const HomePage = () => {
  watchPositionHook();
  return (
    <Layout>
      <Map />
      {/* TODO : Bottom Sheet 만든것 넣기 */}
    </Layout>
  );
};

export default HomePage;
