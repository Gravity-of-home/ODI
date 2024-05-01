import { RouterProvider } from 'react-router-dom';
import './index.css';
import router from './Router.tsx';

function App() {
  return (
    <>
      {/* <h1>OD! Welcome 🍀</h1> */}
      <RouterProvider router={router} />
    </>
  );
}

export default App;
