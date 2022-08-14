import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';

import ErrorBoundary from '@/components/@common/ErrorBoundary/ErrorBoundary';
import NavBar from '@/components/NavBar/NavBar';
import ProtectRoute from '@/components/ProtectRoute';
import SideBar from '@/components/SideBar/SideBar';
import SnackBar from '@/components/SnackBar/SnackBar';
import AuthPage from '@/pages/AuthPage/AuthPage';
import CategoryPage from '@/pages/CategoryPage/CategoryPage';
import MainPage from '@/pages/MainPage/MainPage';

import { PATH } from '@/constants';

function App() {
  return (
    <ErrorBoundary>
      <Router>
        <NavBar />
        <SideBar />
        <Routes>
          <Route path={PATH.MAIN} element={<MainPage />} />
          <Route path={PATH.AUTH} element={<AuthPage />} />
          <Route element={<ProtectRoute />}>
            <Route path={PATH.CATEGORY} element={<CategoryPage />} />
          </Route>
        </Routes>
        <SnackBar />
      </Router>
    </ErrorBoundary>
  );
}

export default App;
