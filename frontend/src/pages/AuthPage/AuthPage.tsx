import { useQuery } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { useRecoilState } from 'recoil';

import { userState } from '@/recoil/atoms';

import { CACHE_KEY, PATH } from '@/constants';

import { setAccessToken } from '@/utils/storage';

import loginApi from '@/api/login';

function AuthPage() {
  const [user, setUserState] = useRecoilState(userState);
  const navigate = useNavigate();

  useQuery<string>(CACHE_KEY.AUTH, loginApi.auth, {
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

  return <div></div>;
}

export default AuthPage;
