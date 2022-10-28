import { lazy, Suspense } from 'react';
import { Outlet, useLocation } from 'react-router-dom';

import useUserValue from '@/hooks/useUserValue';

import SideBarFallback from '@/components/SideBar/SideBar.fallback';
import NotFoundPage from '@/pages/NotFoundPage/NotFoundPage';
import StartPage from '@/pages/StartPage/StartPage';

import { PATH } from '@/constants';

const SideBar = lazy(() => import('@/components/SideBar/SideBar'));

function ProtectRoute() {
  const { isAuthenticating, user } = useUserValue();
  const { pathname } = useLocation();

  if (isAuthenticating) {
    return <></>;
  }

  if (!user.accessToken) {
    return pathname === PATH.MAIN ? <StartPage /> : <NotFoundPage />;
  }

  return (
    <>
      <Suspense fallback={<SideBarFallback />}>
        <SideBar />
      </Suspense>
      <Outlet />
    </>
  );
}

export default ProtectRoute;
