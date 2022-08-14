import useUserValue from '@/hooks/useUserValue';

import CalendarPage from '@/pages/CalendarPage/CalendarPage';
import StartPage from '@/pages/StartPage/StartPage';

function MainPage() {
  const { accessToken } = useUserValue();

  if (!accessToken) {
    return <StartPage />;
  }

  return <CalendarPage />;
}

export default MainPage;
