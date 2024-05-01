import { RouterProvider } from 'react-router-dom';
import './index.css';
import router from './Router.tsx';
// import PartyDetailPage from './pages/Party/PartyDetailPage';

function App() {
  return (
    <>
      {/* <h1>OD! Welcome 🍀</h1> */}
      <RouterProvider router={router} />
      {/* <PartyDetailPage /> */}
    </>
  );
}

export default App;
