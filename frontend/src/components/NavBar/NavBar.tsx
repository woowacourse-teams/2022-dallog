import { useTheme } from '@emotion/react';
import { lazy, Suspense } from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import useToggle from '@/hooks/useToggle';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import ProfileFallback from '@/components/Profile/Profile.fallback';
import SideBarButton from '@/components/SideBarButton/SideBarButton';

import { PATH } from '@/constants';
import { TRANSPARENT } from '@/constants/style';

import { MdCalendarToday, MdOutlineCategory, MdPersonOutline } from 'react-icons/md';

import BlackLogo from '../../assets/dallog_black.png';
import { logo, logoImg, logoText, menu, menus, menuTitle, navBar } from './NavBar.styles';

const Profile = lazy(() => import('@/components/Profile/Profile'));

function NavBar() {
  const { accessToken } = useRecoilValue(userState);

  const theme = useTheme();
  const navigate = useNavigate();

  const { state: isProfileModalOpen, toggleState: toggleProfileModalOpen } = useToggle();

  const handleClickMainButton = () => {
    navigate(PATH.MAIN);
  };

  const handleClickCategoryMenuButton = () => {
    navigate(PATH.CATEGORY);
  };

  const handleClickProfileMenuButton = () => {
    toggleProfileModalOpen();
  };

  return (
    <nav css={navBar}>
      <div css={menus}>
        {accessToken && <SideBarButton />}
        <Button cssProp={logo(theme)} onClick={handleClickMainButton}>
          <img src={BlackLogo} alt="logo" css={logoImg} />
          <span css={logoText}>달록</span>
        </Button>
      </div>
      <div css={menus}>
        {accessToken && (
          <>
            <Button cssProp={menu(theme)} onClick={handleClickMainButton} aria-label="달력 메뉴">
              <MdCalendarToday />
              <span css={menuTitle}>달력</span>
            </Button>
            <Button
              cssProp={menu(theme)}
              onClick={handleClickCategoryMenuButton}
              aria-label="카테고리 메뉴"
            >
              <MdOutlineCategory />
              <span css={menuTitle}>카테고리</span>
            </Button>
            <Button
              cssProp={menu(theme)}
              onClick={handleClickProfileMenuButton}
              aria-label="프로필 메뉴"
              aria-expanded={isProfileModalOpen}
            >
              <MdPersonOutline />
              <span css={menuTitle}>프로필</span>
            </Button>
            <ModalPortal
              isOpen={isProfileModalOpen}
              closeModal={toggleProfileModalOpen}
              dimmerBackground={TRANSPARENT}
            >
              <Suspense fallback={<ProfileFallback />}>
                <Profile />
              </Suspense>
            </ModalPortal>
          </>
        )}
      </div>
    </nav>
  );
}

export default NavBar;
