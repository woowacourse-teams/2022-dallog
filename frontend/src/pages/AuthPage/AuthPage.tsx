import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

import { useAuth } from '@/hooks/@queries/login';

import { PATH } from '@/constants';
import { API } from '@/constants/api';

import { getSearchParam } from '@/utils';

function AuthPage() {
  const navigate = useNavigate();

  const code = getSearchParam(API.AUTH_CODE_KEY);

  const { mutate } = useAuth(code);

  useEffect(() => {
    code && mutate();
    !code && navigate(PATH.MAIN);
  }, []);

  return <div></div>;
}

export default AuthPage;
