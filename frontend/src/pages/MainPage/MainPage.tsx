import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useRecoilValue, useResetRecoilState } from 'recoil';

import { userState } from '@/recoil/atoms';

import CalendarPage from '@/pages/CalendarPage/CalendarPage';
import StartPage from '@/pages/StartPage/StartPage';

import { CACHE_KEY } from '@/constants';
import { RESPONSE } from '@/constants/api';

import { removeAccessToken } from '@/utils/storage';

import loginApi from '@/api/login';

function MainPage() {
  const { accessToken } = useRecoilValue(userState);
  const resetUserState = useResetRecoilState(userState);

  const { isLoading, error } = useQuery<AxiosResponse, AxiosError>(
    CACHE_KEY.VALIDATE,
    () => loginApi.validate(accessToken),
    {
      retry: false,
    }
  );

  if (isLoading) {
    return <></>;
  }

  if (error?.response?.status === RESPONSE.STATUS.UNAUTHORIZED) {
    removeAccessToken();
    resetUserState();

    return <StartPage />;
  }

  return <CalendarPage />;
}

export default MainPage;
