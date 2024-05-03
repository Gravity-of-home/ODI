import { getUserInfo } from '@/apis/User';
import { useQuery } from '@tanstack/react-query';
import useUserStore from '@/stores/useUserStore';

export const useLogin = () => {
  const useGetUserInfo = (props: { userId: string }) => {
    return useQuery({
      queryKey: ['user'],
      queryFn: () => getUserInfo(props.userId),
    });
  };

  // TODO: 쿼리 결과 처리!
  //

  return { useGetUserInfo };
};
