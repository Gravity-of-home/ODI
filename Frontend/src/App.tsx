import { RouterProvider } from 'react-router-dom';
import './index.css';
import { QueryClientProvider } from '@tanstack/react-query';
import queryClient from './utils/QueryClient.ts';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { WebSocketProvider } from './context/webSocketProvider.tsx';
import router from './Router.tsx';

function App() {
  return (
    <>
      {/* <h1>OD! Welcome 🍀</h1> */}
      <QueryClientProvider client={queryClient}>
        <WebSocketProvider>
          <RouterProvider router={router} />
        </WebSocketProvider>
        <ReactQueryDevtools initialIsOpen={false} />
      </QueryClientProvider>
    </>
  );
}

export default App;
