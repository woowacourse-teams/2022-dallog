import { AxiosError } from 'axios';
import { useEffect } from 'react';
import { useMutation } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { useRecoilState } from 'recoil';

import { userState } from '@/recoil/atoms';

import { CACHE_KEY, PATH } from '@/constants';
import { API } from '@/constants/api';

import { getSearchParam } from '@/utils';
import { setAccessToken } from '@/utils/storage';

import loginApi from '@/api/login';

function AuthPage() {
  const [user, setUserState] = useRecoilState(userState);
  const navigate = useNavigate();

  const code = getSearchParam(API.AUTH_CODE_KEY);

  const { mutate } = useMutation<string, AxiosError>(CACHE_KEY.AUTH, () => loginApi.auth(code), {
    onError: () => onErrorAuth(),
    onSuccess: (data) => onSuccessAuth(data),
    useErrorBoundary: true,
  });

  const onErrorAuth = () => {
    navigate(PATH.MAIN);
  };

  const onSuccessAuth = (accessToken: string) => {
    setUserState({ ...user, accessToken });
    setAccessToken(accessToken);

    navigate(PATH.MAIN);
  };

  useEffect(() => {
    code && mutate();
    !code && navigate(PATH.MAIN);
  }, []);

  return <div></div>;
}

export default AuthPage;
