import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useRecoilValue, useResetRecoilState, useSetRecoilState } from 'recoil';

import { sideBarState, userState } from '@/recoil/atoms';

import { CACHE_KEY } from '@/constants';

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
      onSuccess: () => onSuccessValidate(),
      retry: false,
    }
  );

  const onErrorValidate = () => {
    setSideBarOpen(false);
    removeAccessToken();
    resetUser();
  };

  const onSuccessValidate = () => {
    setSideBarOpen(true);
  };

  return { isAuthenticating: isLoading, user };
}

export default useUserValue;
