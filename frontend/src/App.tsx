import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';

import useModal from '@/hooks/useModal';

import NavBar from '@/components/NavBar/NavBar';
import ProtectRoute from '@/components/ProtectRoute';
import AuthPage from '@/pages/AuthPage/AuthPage';
import CategoryPage from '@/pages/CategoryPage/CategoryPage';
import MainPage from '@/pages/MainPage/MainPage';
import MyPage from '@/pages/MyPage/MyPage';

import { PATH } from '@/constants';

function App() {
  const { isOpen, openModal, closeModal } = useModal();

  return (
    <Router>
      <NavBar openLoginModal={openModal} />
      <Routes>
        <Route
          path={PATH.MAIN}
          element={<MainPage isLoginModalOpen={isOpen} closeLoginModal={closeModal} />}
        />
        <Route path={PATH.AUTH} element={<AuthPage />} />
        <Route element={<ProtectRoute />}>
          <Route path={PATH.CATEGORY} element={<CategoryPage />} />
          <Route path={PATH.PROFILE} element={<MyPage />} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
