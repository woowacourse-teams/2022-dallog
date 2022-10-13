import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useRecoilState, useSetRecoilState } from 'recoil';

import { useLoginValidate } from '@/hooks/@queries/login';

import { ProfileType } from '@/@types/profile';

import { sideBarState, userState } from '@/recoil/atoms';

import { CACHE_KEY } from '@/constants/api';

import { removeAccessToken, removeRefreshToken } from '@/utils/storage';

import profileApi from '@/api/profile';

function useUserValue() {
  const [user, setUser] = useRecoilState(userState);
  const setSideBarOpen = useSetRecoilState(sideBarState);

  const { isLoading, isSuccess } = useLoginValidate();

  useQuery<AxiosResponse<ProfileType>, AxiosError>(
    CACHE_KEY.PROFILE,
    () => profileApi.get(user.accessToken),
    {
      onError: () => onErrorValidate(),
      onSuccess: ({ data }) => setUser({ ...user, ...data }),
      retry: false,
      useErrorBoundary: false,
      enabled: isSuccess,
      staleTime: 1 * 60 * 1000,
    }
  );

  const onErrorValidate = () => {
    setUser({ accessToken: '', refreshToken: '' });
    setSideBarOpen(false);
    removeAccessToken();
    removeRefreshToken();
  };

  return { isAuthenticating: isLoading, user };
}

export default useUserValue;
