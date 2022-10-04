import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQuery } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { useRecoilState, useSetRecoilState } from 'recoil';

import { sideBarState, userState } from '@/recoil/atoms';

import { PATH } from '@/constants';
import { CACHE_KEY } from '@/constants/api';

import { removeAccessToken, setAccessToken } from '@/utils/storage';

import loginApi from '@/api/login';

function useGetLoginUrl() {
  const { error, refetch } = useQuery<string>(CACHE_KEY.ENTER, loginApi.getUrl, {
    enabled: false,
    onSuccess: (data) => onSuccessGetLoginUrl(data),
  });

  const onSuccessGetLoginUrl = (loginUrl: string) => {
    location.href = loginUrl;
  };

  return {
    error,
    refetch,
  };
}

function useAuth(code: string | null) {
  const [user, setUser] = useRecoilState(userState);
  const navigate = useNavigate();

  const { mutate } = useMutation<string, AxiosError>(() => loginApi.auth(code), {
    onError: () => onErrorAuth(),
    onSuccess: (data) => onSuccessAuth(data),
  });

  const onErrorAuth = () => {
    navigate(PATH.MAIN);
  };

  const onSuccessAuth = (accessToken: string) => {
    setUser({ ...user, accessToken });
    setAccessToken(accessToken);

    navigate(PATH.MAIN);
  };

  return {
    mutate,
  };
}

function useLoginValidate() {
  const [user, setUser] = useRecoilState(userState);
  const setSideBarOpen = useSetRecoilState(sideBarState);

  const { isLoading, isSuccess } = useQuery<AxiosResponse, AxiosError>(
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
    setUser({ accessToken: '' });
    setSideBarOpen(false);
    removeAccessToken();
  };

  return {
    isLoading,
    isSuccess,
  };
}

export { useGetLoginUrl, useAuth, useLoginValidate };
