import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';

import AuthPage from '@/pages/AuthPage/AuthPage';
import CategoryPage from '@/pages/CategoryPage/CategoryPage';
import MainPage from '@/pages/MainPage/MainPage';
import MyPage from '@/pages/MyPage/MyPage';

import { PATH } from '@/constants';

function App() {
  return (
    <Router>
      <Routes>
        <Route path={PATH.MAIN} element={<MainPage />} />
        <Route path={PATH.AUTH} element={<AuthPage />} />
        <Route path={PATH.CATEGORY} element={<CategoryPage />} />
        <Route path={PATH.PROFILE} element={<MyPage />} />
      </Routes>
    </Router>
  );
}

export default App;
