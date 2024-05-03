import { useEffect } from 'react';
import { getUserInfo } from '@/apis/User';
// import { beforeReq, refreshJWT } from '@/utils/JWTUtil';
import { useQuery } from '@tanstack/react-query';
import { UseQueryCustomOptions } from '@/types/common';

const useGetRefreshToken = () => {
  const { isSuccess, isError } = useQuery({
    queryKey: ['auth', 'getAccessToken'],
    queryFn: refreshJWT,
    staleTime: 12 * 60 * 60 * 1000, // 유지 시간 12시간
    refetchInterval: 12 * 60 * 60 * 1000, // 12시간마다 재요청
    refetchOnReconnect: true, // 다시 연결되는 경우 재요청
    refetchIntervalInBackground: true, // 백그라운드에서도 재요청
  });

  useEffect(() => {
    if (isSuccess) {
      console.log('Refresh Token Success');
    }
  }, [isSuccess]);

  useEffect(() => {
    if (isError) {
      console.log('Refresh Token Error');
    }
  }, [isError]);

  return { isSuccess, isError };
};

const useGetUserInfo = (queryOptions?: UseQueryCustomOptions) => {
  return useQuery({
    queryKey: ['auth', 'getUserInfo'],
    queryFn: getUserInfo,
    ...queryOptions,
  });
};

const useAuth = () => {
  const refreshTokenQuery = useGetRefreshToken();
  const getUserInfoQuery = useGetUserInfo();

  return { getUserInfoQuery, refreshTokenQuery };
};

export default useAuth;
