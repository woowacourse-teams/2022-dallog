import { useEffect } from 'react';
import { useRecoilState } from 'recoil';

import { userState } from '@/atoms';

import CalendarPage from '@/pages/CalendarPage/CalendarPage';
import StartPage from '@/pages/StartPage/StartPage';

import { getAccessToken } from '@/utils';

function MainPage() {
  const [user, setUser] = useRecoilState(userState);

  useEffect(() => {
    setUser({ ...user, accessToken: getAccessToken() });
  }, []);

  if (!user.accessToken) {
    return <StartPage />;
  }

  return <CalendarPage />;
}

export default MainPage;
