import { useQuery } from 'react-query';
import { useNavigate } from 'react-router-dom';

import { CACHE_KEY, PATH } from '@/constants';

import { setAccessToken } from '@/utils';

import loginApi from '@/api/login';

function AuthPage() {
  const navigate = useNavigate();

  useQuery<string>(CACHE_KEY.AUTH, loginApi.auth, {
    onError: () => onErrorAuth(),
    onSuccess: (data) => onSuccessAuth(data),
  });

  const onErrorAuth = () => {
    navigate(PATH.MAIN);
  };

  const onSuccessAuth = (accessToken: string) => {
    setAccessToken(accessToken);
    navigate(PATH.MAIN);
  };

  return <div></div>;
}

export default AuthPage;
