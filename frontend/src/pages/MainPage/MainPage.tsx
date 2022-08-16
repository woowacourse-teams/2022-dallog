import useUserValue from '@/hooks/useUserValue';

import CalendarPage from '@/pages/CalendarPage/CalendarPage';
import StartPage from '@/pages/StartPage/StartPage';

function MainPage() {
  const { isAuthenticating, user } = useUserValue();

  if (isAuthenticating) {
    return <></>;
  }

  if (!user.accessToken) {
    return <StartPage />;
  }

  return <CalendarPage />;
}

export default MainPage;
