import { useRecoilValue } from 'recoil';

import { userState } from '@/recoil/atoms';

import CalendarPage from '@/pages/CalendarPage/CalendarPage';
import StartPage from '@/pages/StartPage/StartPage';

function MainPage() {
  const { accessToken } = useRecoilValue(userState);

  if (!accessToken) {
    return <StartPage />;
  }

  return <CalendarPage />;
}

export default MainPage;
