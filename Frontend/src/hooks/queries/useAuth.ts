import { useEffect } from 'react';
import { refreshJWT } from '@/utils/JWTUtil';
import { getUserInfo } from '@/apis/User';
import { useQuery, useMutation } from '@tanstack/react-query';
import { UseMutationCustomOptions, UseQueryCustomOptions } from '@/types/common';
import { removeHeader, setHeader } from '@/utils/HeaderUtil';
import { IUser } from '@/types/User';
// import queryClient from '@/utils/QueryClient';

// const useLogin = (mutatuionOptions?: UseMutationCustomOptions) => {
//   return useMutation({
//     mutationFn: refreshJWT,
//     onSuccess: data => {
//       console.log(data);
//       setHeader('AUTHORIZATION', `Bearer ${data}`);
//     },
//     onSettled: () => {
//       queryClient.refetchQueries({ queryKey: ['auth', 'getAccessToken'] });
//       queryClient.invalidateQueries({ queryKey: ['auth', 'getUserInfo'] });
//     },
//     ...mutatuionOptions,
//   });
// };

const useGetRefreshToken = () => {
  const { isSuccess, data, isError } = useQuery({
    queryKey: ['auth', 'getAccessToken'],
    queryFn: refreshJWT,
    // staleTime: 12 * 60 * 60 * 1000, // 유지 시간 12시간
    staleTime: 60 * 1000, // 유지 시간 12시간
    // refetchInterval: 12 * 60 * 60 * 1000, // 12시간마다 재요청
    refetchInterval: 60 * 1000, // 12시간마다 재요청
    refetchOnReconnect: true, // 다시 연결되는 경우 재요청
    refetchIntervalInBackground: true, // 백그라운드에서도 재요청
  });

  useEffect(() => {
    if (isSuccess) {
      setHeader('AUTHORIZATION', `Bearer ${data}`);
      console.log('Refresh Token Success');
    }
  }, [isSuccess]);

  useEffect(() => {
    if (isError) {
      removeHeader('AUTHORIZATION');
      console.log('Refresh Token Error');
    }
  }, [isError]);

  return { isSuccess, data, isError };
};

const useGetUserInfo = (queryOptions?: UseQueryCustomOptions) => {
  const { isSuccess, data, isError } = useQuery({
    queryKey: ['auth', 'getUserInfo'],
    queryFn: getUserInfo,
    ...queryOptions,
  });

  const userData = data as IUser;

  return { isSuccess, userData, isError };
};

const useAuth = () => {
  const refreshTokenQuery = useGetRefreshToken();
  const getUserInfoQuery = useGetUserInfo({
    enabled: refreshTokenQuery.isSuccess,
  });

  return { getUserInfoQuery, refreshTokenQuery };
};

export default useAuth;
