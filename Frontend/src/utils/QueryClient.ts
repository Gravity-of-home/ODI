import { QueryClient } from '@tanstack/react-query';

const queryClient = new QueryClient({
  /** NOTE:
   * Query는 요청이 실패하면 기본적으로 3번 재요청을 하게 되는데
   * 재요청을 하지 않겠다는 설정!
   * */
  defaultOptions: {
    queries: {
      retry: false,
    },
    mutations: {
      retry: false,
    },
  },
});

export default queryClient;
