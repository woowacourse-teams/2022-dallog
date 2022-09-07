import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useRecoilValue, useResetRecoilState, useSetRecoilState } from 'recoil';

import { sideBarState, userState } from '@/recoil/atoms';

import { CACHE_KEY } from '@/constants/api';

import { removeAccessToken } from '@/utils/storage';

import loginApi from '@/api/login';

function useUserValue() {
  const user = useRecoilValue(userState);
  const resetUser = useResetRecoilState(userState);

  const setSideBarOpen = useSetRecoilState(sideBarState);

  const { isLoading } = useQuery<AxiosResponse, AxiosError>(
    CACHE_KEY.VALIDATE,
    () => loginApi.validate(user.accessToken),
    {
      onError: () => onErrorValidate(),
      retry: false,
      useErrorBoundary: false,
      enabled: !!user.accessToken,
    }
  );

  const onErrorValidate = () => {
    setSideBarOpen(false);
    removeAccessToken();
    resetUser();
  };

  return { isAuthenticating: isLoading, user };
}

export default useUserValue;
