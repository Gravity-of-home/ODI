// import getUserInfoQuery
import { useEffect } from 'react';
import Map from '@/components/Maps/Map';
import { Layout } from '@/components/Layout';

const HomePage = () => {
  useEffect(() => {
    // const userInfo = getUserInfo();
    // console.log('USER INFO: ', userInfo);
  }, []);
  return (
    <Layout>
      <Map />
    </Layout>
  );
};

export default HomePage;
