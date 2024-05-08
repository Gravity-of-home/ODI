import { RouterProvider } from 'react-router-dom';
import './index.css';
import { QueryClientProvider } from '@tanstack/react-query';
import queryClient from './utils/QueryClient.ts';
import { Layout } from './components/Layout.tsx';
import router from './Router.tsx';
import { ToastContainer } from 'react-toastify';
function App() {
  return (
    <>
      {/* <h1>OD! Welcome 🍀</h1> */}
      <QueryClientProvider client={queryClient}>
        <Layout>
          <ToastContainer />
          <RouterProvider router={router} />
        </Layout>
      </QueryClientProvider>
    </>
  );
}

export default App;
