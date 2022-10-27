import { AxiosError } from 'axios';
import { lazy, Suspense } from 'react';
import { useIsMutating, useQueryClient } from 'react-query';
import { Route, Routes } from 'react-router-dom';

import { useLoginAgain } from '@/hooks/@queries/login';
import useSnackBar from '@/hooks/useSnackBar';

import NavBar from '@/components/NavBar/NavBar';
import ProtectRoute from '@/components/ProtectRoute/ProtectRoute';
import SnackBar from '@/components/SnackBar/SnackBar';
import CalendarPage from '@/pages/CalendarPage/CalendarPage';
import CategoryPage from '@/pages/CategoryPage/CategoryPage';
import StartPage from '@/pages/StartPage/StartPage';

import { PATH } from '@/constants';
import { CACHE_KEY, RESPONSE } from '@/constants/api';
import { ERROR_MESSAGE } from '@/constants/message';

const AuthPage = lazy(() => import('@/pages/AuthPage/AuthPage'));
const NotFoundPage = lazy(() => import('@/pages/NotFoundPage/NotFoundPage'));
const PrivacyPolicyPage = lazy(() => import('@/pages/PrivacyPolicyPage/PrivacyPolicyPage'));

function App() {
  const { openSnackBar } = useSnackBar();

  const queryClient = useQueryClient();
  const isMutatingLoginAgain = useIsMutating(CACHE_KEY.LOGIN_AGAIN);

  const { mutate } = useLoginAgain();

  const onError = (error: unknown) => {
    if (error instanceof AxiosError && error.response?.status === RESPONSE.STATUS.UNAUTHORIZED) {
      !isMutatingLoginAgain && mutate();

      return;
    }

    error instanceof AxiosError
      ? openSnackBar(error.response?.data.message ?? ERROR_MESSAGE.DEFAULT)
      : openSnackBar(ERROR_MESSAGE.DEFAULT);
  };

  queryClient.setDefaultOptions({
    queries: {
      retry: 1,
      retryDelay: 0,
      onError,
      staleTime: 1 * 60 * 1000,
    },
    mutations: {
      retry: 1,
      retryDelay: 0,
      onError,
    },
  });

  return (
    <Suspense fallback={<></>}>
      <NavBar />
      <Routes>
        <Route element={<ProtectRoute />}>
          <Route path={PATH.MAIN} element={<CalendarPage />} />
          <Route path={PATH.CATEGORY} element={<CategoryPage />} />
          <Route path="*" element={<NotFoundPage />} />
        </Route>
        <Route path={PATH.MAIN} element={<StartPage />} />
        <Route path={PATH.AUTH} element={<AuthPage />} />
        <Route path={PATH.POLICY} element={<PrivacyPolicyPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
      <SnackBar />
    </Suspense>
  );
}

export default App;
