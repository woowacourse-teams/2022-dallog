import { lazy, Suspense } from 'react';
import { Outlet } from 'react-router-dom';

import useUserValue from '@/hooks/useUserValue';

import SideBarFallback from '@/components/SideBar/SideBar.fallback';
import StartPage from '@/pages/StartPage/StartPage';

const SideBar = lazy(() => import('@/components/SideBar/SideBar'));

function ProtectRoute() {
  const { isAuthenticating, user } = useUserValue();

  if (isAuthenticating) {
    return <></>;
  }

  if (!user.accessToken) {
    return <StartPage />;
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
