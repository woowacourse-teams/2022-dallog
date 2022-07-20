import { Navigate, Outlet } from 'react-router-dom';
import { useRecoilState } from 'recoil';

import { userState } from '@/atoms';

import { PATH } from '@/constants';

function ProtectRoute() {
  const [user] = useRecoilState(userState);

  if (!user.accessToken) {
    return <Navigate to={PATH.MAIN} replace />;
  }

  return <Outlet />;
}

export default ProtectRoute;
