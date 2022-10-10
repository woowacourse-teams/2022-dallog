import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQuery } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { useRecoilState, useRecoilValue, useSetRecoilState } from 'recoil';

import { sideBarState, userState, UserStateType } from '@/recoil/atoms';

import { PATH } from '@/constants';
import { CACHE_KEY } from '@/constants/api';

import {
  removeAccessToken,
  removeRefreshToken,
  setAccessToken,
  setRefreshToken,
} from '@/utils/storage';

import loginApi from '@/api/login';

function useAuth(code: string | null) {
  const [user, setUser] = useRecoilState(userState);
  const navigate = useNavigate();

  const { mutate } = useMutation<UserStateType, AxiosError>(() => loginApi.auth(code), {
    onError: () => onErrorAuth(),
    onSuccess: ({ accessToken, refreshToken }) => {
      onSuccessAuth(accessToken, refreshToken);
    },
  });

  const onErrorAuth = () => {
    navigate(PATH.MAIN);
  };

  const onSuccessAuth = (accessToken: string, refreshToken: string) => {
    setUser({ ...user, accessToken, refreshToken });
    setAccessToken(accessToken);
    setRefreshToken(refreshToken);

    navigate(PATH.MAIN);
  };

  return {
    mutate,
  };
}

function useLoginAgain() {
  const { refreshToken } = useRecoilValue(userState);
  const navigate = useNavigate();

  const { mutate } = useMutation<string, AxiosError>(() => loginApi.again(refreshToken), {
    onSuccess: (data) => {
      setAccessToken(data);
    },
    onError: () => {
      removeAccessToken();
      removeRefreshToken();
      navigate(PATH.MAIN);
      location.reload();
    },
  });

  return {
    mutate,
  };
}

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
    setUser({ accessToken: '', refreshToken: '' });
    setSideBarOpen(false);
    removeAccessToken();
    removeRefreshToken();
  };

  return {
    isLoading,
    isSuccess,
  };
}

export { useAuth, useGetLoginUrl, useLoginAgain, useLoginValidate };
