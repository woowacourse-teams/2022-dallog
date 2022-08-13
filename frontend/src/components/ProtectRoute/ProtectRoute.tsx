import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { Navigate, Outlet } from 'react-router-dom';
import { useRecoilValue, useResetRecoilState } from 'recoil';

import { userState } from '@/recoil/atoms';

import { CACHE_KEY, PATH } from '@/constants';
import { RESPONSE } from '@/constants/api';

import { removeAccessToken } from '@/utils/storage';

import loginApi from '@/api/login';

function ProtectRoute() {
  const { accessToken } = useRecoilValue(userState);
  const resetUserState = useResetRecoilState(userState);

  const { isLoading, error } = useQuery<AxiosResponse, AxiosError>(
    CACHE_KEY.VALIDATE,
    () => loginApi.validate(accessToken),
    {
      retry: false,
    }
  );

  if (isLoading) {
    return <></>;
  }

  if (error?.response?.status === RESPONSE.STATUS.UNAUTHORIZED) {
    removeAccessToken();
    resetUserState();

    return <Navigate to={PATH.MAIN} replace />;
  }

  return <Outlet />;
}

export default ProtectRoute;
