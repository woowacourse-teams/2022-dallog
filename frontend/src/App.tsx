import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';

import useToggle from '@/hooks/useToggle';

import NavBar from '@/components/NavBar/NavBar';
import ProtectRoute from '@/components/ProtectRoute';
import SideBar from '@/components/SideBar/SideBar';
import AuthPage from '@/pages/AuthPage/AuthPage';
import CategoryPage from '@/pages/CategoryPage/CategoryPage';
import MainPage from '@/pages/MainPage/MainPage';
import MyPage from '@/pages/MyPage/MyPage';

import { PATH } from '@/constants';

function App() {
  const { state: isLoginModalOpen, toggleState: toggleLoginModalOpen } = useToggle();
  const { state: isSideBarOpen, toggleState: toggleSideBarOpen } = useToggle();

  return (
    <Router>
      <NavBar
        isSideBarOpen={isSideBarOpen}
        toggleSideBarOpen={toggleSideBarOpen}
        openLoginModal={toggleLoginModalOpen}
      />
      <SideBar isOpen={isSideBarOpen} />
      <Routes>
        <Route
          path={PATH.MAIN}
          element={
            <MainPage isLoginModalOpen={isLoginModalOpen} closeLoginModal={toggleLoginModalOpen} />
          }
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
