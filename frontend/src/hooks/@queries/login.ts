import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQuery } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { useRecoilState, useSetRecoilState } from 'recoil';

import useSnackBar from '@/hooks/useSnackBar';

import { sideBarState, userState, UserStateType } from '@/recoil/atoms';

import { PATH } from '@/constants';
import { CACHE_KEY, RESPONSE } from '@/constants/api';
import { SUCCESS_MESSAGE } from '@/constants/message';

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

function useLoginAgain() {
  const [user, setUser] = useRecoilState(userState);
  const { openSnackBar } = useSnackBar();

  const navigate = useNavigate();

  const { mutate } = useMutation<string, AxiosError>(() => loginApi.again(user.refreshToken), {
    onSuccess: (data) => {
      setAccessToken(data);
      setUser({ ...user, accessToken: data });
      openSnackBar(SUCCESS_MESSAGE.LOGIN_AGAIN);
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

function useLoginValidate() {
  const [user, setUser] = useRecoilState(userState);
  const setSideBarOpen = useSetRecoilState(sideBarState);

  const { mutate } = useLoginAgain();

  const { isLoading, isSuccess } = useQuery<AxiosResponse, AxiosError>(
    [CACHE_KEY.VALIDATE, user.accessToken],
    () => loginApi.validate(user.accessToken),
    {
      onError: (error: unknown) => onErrorValidate(error),
      retry: false,
      useErrorBoundary: false,
      enabled: !!user.accessToken,
    }
  );

  const onErrorValidate = (error: unknown) => {
    if (error instanceof AxiosError && error.response?.status === RESPONSE.STATUS.UNAUTHORIZED) {
      mutate();

      return;
    }

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
