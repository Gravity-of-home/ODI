import { RouterProvider } from 'react-router-dom';
import './index.css';
import { QueryClientProvider } from '@tanstack/react-query';
import queryClient from './utils/QueryClient.ts';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { Layout } from './components/Layout.tsx';
import router from './Router.tsx';

function App() {
  return (
    <>
      {/* <h1>OD! Welcome 🍀</h1> */}
      <QueryClientProvider client={queryClient}>
        <Layout>
          <RouterProvider router={router} />
        </Layout>
        <ReactQueryDevtools initialIsOpen={false} />
      </QueryClientProvider>
    </>
  );
}

export default App;
