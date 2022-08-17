import { QueryClient, QueryClientProvider } from 'react-query';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';

import ErrorBoundary from '@/components/@common/ErrorBoundary/ErrorBoundary';
import NavBar from '@/components/NavBar/NavBar';
import ProtectRoute from '@/components/ProtectRoute/ProtectRoute';
import PublicRoute from '@/components/PublicRoute/PublicRoute';
import SideBar from '@/components/SideBar/SideBar';
import SnackBar from '@/components/SnackBar/SnackBar';
import AuthPage from '@/pages/AuthPage/AuthPage';
import CategoryPage from '@/pages/CategoryPage/CategoryPage';
import MainPage from '@/pages/MainPage/MainPage';
import NotFoundPage from '@/pages/NotFoundPage/NotFoundPage';
import PrivacyPolicyPage from '@/pages/PrivacyPolicyPage/PrivacyPolicyPage';

import { PATH } from '@/constants';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      useErrorBoundary: true,
      retry: 1,
      retryDelay: 0,
    },
    mutations: {
      useErrorBoundary: true,
      retry: 1,
      retryDelay: 0,
    },
  },
});

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <ErrorBoundary>
        <Router>
          <NavBar />
          <SideBar />
          <Routes>
            <Route element={<PublicRoute />}>
              <Route path={PATH.MAIN} element={<MainPage />} />
              <Route path={PATH.AUTH} element={<AuthPage />} />
              <Route path={PATH.POLICY} element={<PrivacyPolicyPage />} />
            </Route>
            <Route element={<ProtectRoute />}>
              <Route path={PATH.CATEGORY} element={<CategoryPage />} />
            </Route>
            <Route path="*" element={<NotFoundPage />} />
          </Routes>
          <SnackBar />
        </Router>
      </ErrorBoundary>
    </QueryClientProvider>
  );
}

export default App;
