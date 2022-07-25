import { Navigate, Outlet } from 'react-router-dom';
import { useRecoilState } from 'recoil';

import { userState } from '@/recoil/atoms';

import { PATH } from '@/constants';

function ProtectRoute() {
  const [user] = useRecoilState(userState);

  if (!user.accessToken) {
    return <Navigate to={PATH.MAIN} replace />;
  }

  return <Outlet />;
}

export default ProtectRoute;
