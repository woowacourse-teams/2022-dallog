import { AxiosError, AxiosResponse } from 'axios';
import { useEffect, useState } from 'react';
import { useMutation } from 'react-query';
import { useRecoilState, useRecoilValue, useResetRecoilState } from 'recoil';

import { userState } from '@/recoil/atoms';

import { CACHE_KEY } from '@/constants';

import { removeAccessToken } from '@/utils/storage';

import loginApi from '@/api/login';

const defaultUserState = {
  accessToken: '',
};

function useUserValue() {
  const userAtom = useRecoilValue(userState);
  const resetUserAtom = useResetRecoilState(userState);

  const [user, setUser] = useState(defaultUserState);

  const { mutate } = useMutation<AxiosResponse, AxiosError>(
    CACHE_KEY.VALIDATE,
    () => loginApi.validate(userAtom.accessToken),
    {
      retry: false,
      onError: () => onErrorValidate(),
      onSuccess: () => onSuccessValidate(),
    }
  );

  const onErrorValidate = () => {
    setUser(defaultUserState);
    removeAccessToken();
    resetUserAtom();
  };

  const onSuccessValidate = () => {
    setUser(userAtom);
  };

  useEffect(() => {
    userAtom.accessToken && mutate();
  }, []);

  return user;
}

export default useUserValue;
