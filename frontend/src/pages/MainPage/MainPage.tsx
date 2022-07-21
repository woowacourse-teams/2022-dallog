import { useEffect } from 'react';
import { useRecoilState } from 'recoil';

import { userState } from '@/atoms';

import CalendarPage from '@/pages/CalendarPage/CalendarPage';
import StartPage from '@/pages/StartPage/StartPage';

import { getAccessToken } from '@/utils';

interface MainPageProps {
  isLoginModalOpen: boolean;
  closeLoginModal: () => void;
}

function MainPage({ isLoginModalOpen, closeLoginModal }: MainPageProps) {
  const [user, setUser] = useRecoilState(userState);

  useEffect(() => {
    setUser({ ...user, accessToken: getAccessToken() });
  }, []);

  if (!user.accessToken) {
    return <StartPage isLoginModalOpen={isLoginModalOpen} closeLoginModal={closeLoginModal} />;
  }

  return <CalendarPage />;
}

export default MainPage;
