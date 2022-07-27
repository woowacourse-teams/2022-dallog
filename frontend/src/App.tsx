import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';

import NavBar from '@/components/NavBar/NavBar';
import ProtectRoute from '@/components/ProtectRoute';
import SideBar from '@/components/SideBar/SideBar';
import SnackBar from '@/components/SnackBar/SnackBar';
import AuthPage from '@/pages/AuthPage/AuthPage';
import CategoryPage from '@/pages/CategoryPage/CategoryPage';
import LoginPage from '@/pages/LoginPage/LoginPage';
import MainPage from '@/pages/MainPage/MainPage';
import MyPage from '@/pages/MyPage/MyPage';

import { PATH } from '@/constants';

function App() {
  return (
    <Router>
      <NavBar />
      <SideBar />
      <Routes>
        <Route path={PATH.MAIN} element={<MainPage />} />
        <Route path={PATH.AUTH} element={<AuthPage />} />
        <Route path={PATH.LOGIN} element={<LoginPage />} />
        <Route element={<ProtectRoute />}>
          <Route path={PATH.CATEGORY} element={<CategoryPage />} />
          <Route path={PATH.PROFILE} element={<MyPage />} />
        </Route>
      </Routes>
      <SnackBar />
    </Router>
  );
}

export default App;
