import { Outlet } from 'react-router-dom';

import useUserValue from '@/hooks/useUserValue';

function PublicRoute() {
  const { isAuthenticating } = useUserValue();

  if (isAuthenticating) {
    return <></>;
  }

  return <Outlet />;
}

export default PublicRoute;
