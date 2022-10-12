import { AxiosError } from 'axios';
import { lazy, Suspense } from 'react';
import { QueryClient, QueryClientProvider } from 'react-query';
import { ReactQueryDevtools } from 'react-query/devtools';
import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';

import useSnackBar from '@/hooks/useSnackBar';

import ErrorBoundary from '@/components/@common/ErrorBoundary/ErrorBoundary';

import { PATH } from '@/constants';

import { ERROR_MESSAGE } from './constants/message';

const NavBar = lazy(() => import('@/components/NavBar/NavBar'));
const ProtectRoute = lazy(() => import('@/components/ProtectRoute/ProtectRoute'));
const SideBar = lazy(() => import('@/components/SideBar/SideBar'));
const SnackBar = lazy(() => import('@/components/SnackBar/SnackBar'));
const AuthPage = lazy(() => import('@/pages/AuthPage/AuthPage'));
const CategoryPage = lazy(() => import('@/pages/CategoryPage/CategoryPage'));
const MainPage = lazy(() => import('@/pages/MainPage/MainPage'));
const NotFoundPage = lazy(() => import('@/pages/NotFoundPage/NotFoundPage'));
const PrivacyPolicyPage = lazy(() => import('@/pages/PrivacyPolicyPage/PrivacyPolicyPage'));

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
        staleTime: 1 * 60 * 1000,
        cacheTime: 5 * 60 * 1000,
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
          <Suspense fallback={<></>}>
            <NavBar />
            <SideBar />
            <Routes>
              <Route path={PATH.MAIN} element={<MainPage />} />
              <Route path={PATH.AUTH} element={<AuthPage />} />
              <Route path={PATH.POLICY} element={<PrivacyPolicyPage />} />
              <Route path="*" element={<NotFoundPage />} />
              <Route element={<ProtectRoute />}>
                <Route path={PATH.CATEGORY} element={<CategoryPage />} />
              </Route>
            </Routes>
          </Suspense>
          <SnackBar />
        </Router>
      </ErrorBoundary>
      <ReactQueryDevtools initialIsOpen={false} />
    </QueryClientProvider>
  );
}

export default App;
