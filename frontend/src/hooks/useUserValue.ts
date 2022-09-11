import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useRecoilState, useResetRecoilState, useSetRecoilState } from 'recoil';

import { ProfileType } from '@/@types/profile';

import { sideBarState, userState } from '@/recoil/atoms';

import { CACHE_KEY } from '@/constants/api';

import { removeAccessToken } from '@/utils/storage';

import loginApi from '@/api/login';
import profileApi from '@/api/profile';

function useUserValue() {
  const [user, setUser] = useRecoilState(userState);
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

  useQuery<AxiosResponse<ProfileType>, AxiosError>(
    CACHE_KEY.PROFILE,
    () => profileApi.get(user.accessToken),
    {
      onError: () => onErrorValidate(),
      onSuccess: ({ data }) => setUser({ ...user, ...data }),
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
