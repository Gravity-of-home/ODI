import { RouterProvider } from 'react-router-dom';
import './index.css';
import { QueryClientProvider } from '@tanstack/react-query';
import queryClient from './utils/QueryClient.ts';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { Layout } from './components/Layout.tsx';
import router from './Router.tsx';
import { ToastContainer } from 'react-toastify';
function App() {
  return (
    <>
      {/* <h1>OD! Welcome 🍀</h1> */}
      <QueryClientProvider client={queryClient}>
        <Layout>
          <ToastContainer
            position='top-center'
            autoClose={2000}
            hideProgressBar={false}
            newestOnTop={true}
            closeOnClick
            rtl={false}
            pauseOnFocusLoss
            draggable
            pauseOnHover
            theme='light'
          />
          <RouterProvider router={router} />
        </Layout>
        <ReactQueryDevtools initialIsOpen={false} />
      </QueryClientProvider>
    </>
  );
}

export default App;
