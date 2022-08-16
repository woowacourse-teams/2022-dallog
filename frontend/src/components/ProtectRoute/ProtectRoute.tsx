import { Navigate, Outlet } from 'react-router-dom';

import useUserValue from '@/hooks/useUserValue';

import { PATH } from '@/constants';

function ProtectRoute() {
  const { isAuthenticating, user } = useUserValue();

  if (isAuthenticating) {
    return <></>;
  }

  if (!user.accessToken) {
    return <Navigate to={PATH.MAIN} replace />;
  }

  return <Outlet />;
}

export default ProtectRoute;
