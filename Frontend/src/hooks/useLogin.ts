import { getUserInfo } from '@/apis/User';
import { useQuery } from '@tanstack/react-query';

export const useLogin = () => {
  const useGetUserInfo = (props: { userId: string }) => {
    return useQuery({
      queryKey: ['user'],
      queryFn: () => getUserInfo(),
    });
  };

  // TODO: 쿼리 결과 처리!
  //

  return { useGetUserInfo };
};
