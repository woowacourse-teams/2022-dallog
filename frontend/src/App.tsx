import { AxiosError } from 'axios';
import { QueryClient, QueryClientProvider } from 'react-query';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';

import useSnackBar from '@/hooks/useSnackBar';

import ErrorBoundary from '@/components/@common/ErrorBoundary/ErrorBoundary';
import NavBar from '@/components/NavBar/NavBar';
import ProtectRoute from '@/components/ProtectRoute/ProtectRoute';
import SideBar from '@/components/SideBar/SideBar';
import SnackBar from '@/components/SnackBar/SnackBar';
import AuthPage from '@/pages/AuthPage/AuthPage';
import CategoryPage from '@/pages/CategoryPage/CategoryPage';
import MainPage from '@/pages/MainPage/MainPage';
import NotFoundPage from '@/pages/NotFoundPage/NotFoundPage';
import PrivacyPolicyPage from '@/pages/PrivacyPolicyPage/PrivacyPolicyPage';
import SchedulingPage from '@/pages/SchedulingPage/SchedulingPage';

import { PATH } from '@/constants';

import { ERROR_MESSAGE } from './constants/message';

function App() {
  const { openSnackBar } = useSnackBar();

  const onError = (error: unknown) =>
    error instanceof AxiosError
      ? openSnackBar(error.response?.data.message ?? ERROR_MESSAGE.DEFAULT)
      : openSnackBar(ERROR_MESSAGE.DEFAULT);

  const queryClient = new QueryClient({
    defaultOptions: {
      queries: {
        retry: 1,
        retryDelay: 0,
        onError,
      },
      mutations: {
        retry: 1,
        retryDelay: 0,
        onError,
      },
    },
  });

  return (
    <QueryClientProvider client={queryClient}>
      <ErrorBoundary>
        <Router>
          <NavBar />
          <SideBar />
          <Routes>
            <Route path={PATH.MAIN} element={<MainPage />} />
            <Route path={PATH.AUTH} element={<AuthPage />} />
            <Route path={PATH.POLICY} element={<PrivacyPolicyPage />} />
            <Route path="*" element={<NotFoundPage />} />
            <Route element={<ProtectRoute />}>
              <Route path={PATH.CATEGORY} element={<CategoryPage />} />
              <Route path={PATH.SCHEDULING} element={<SchedulingPage />} />
            </Route>
          </Routes>
          <SnackBar />
        </Router>
      </ErrorBoundary>
    </QueryClientProvider>
  );
}

export default App;
