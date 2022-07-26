import { useTheme } from '@emotion/react';
import { useNavigate } from 'react-router-dom';
import { useRecoilState, useRecoilValue } from 'recoil';

import { userState } from '@/recoil/atoms';
import { sideBarSelector } from '@/recoil/selectors';

import Button from '@/components/@common/Button/Button';

import { PATH } from '@/constants';

import { BiCategory } from 'react-icons/bi';
import { FaUserCircle } from 'react-icons/fa';
import { FiCalendar } from 'react-icons/fi';
import { HiChevronDoubleLeft, HiMenu } from 'react-icons/hi';

import { loginButton, logo, menu, menus, menuTitle, navBar } from './NavBar.styles';

function NavBar() {
  const [isSideBarOpen, toggleSideBarOpen] = useRecoilState(sideBarSelector);
  const { accessToken } = useRecoilValue(userState);
  const theme = useTheme();
  const navigate = useNavigate();

  const handleClickSideBarButton = () => {
    toggleSideBarOpen(isSideBarOpen);
  };

  const handleClickMainButton = () => {
    navigate(PATH.MAIN);
  };

  const handleClickCategoryMenuButton = () => {
    navigate(PATH.CATEGORY);
  };

  const handleClickProfileMenuButton = () => {
    navigate(PATH.PROFILE);
  };

  const handleClickLoginButton = () => {
    navigate(PATH.LOGIN);
  };

  return (
    <div css={navBar}>
      <div css={menus}>
        <Button cssProp={menu(theme)} onClick={handleClickSideBarButton}>
          {isSideBarOpen ? <HiChevronDoubleLeft size={28} /> : <HiMenu size={28} />}
          <span css={menuTitle}>메뉴</span>
        </Button>
        <Button cssProp={logo(theme)} onClick={handleClickMainButton}>
          🌙&nbsp;&nbsp;달록
        </Button>
      </div>
      <div css={menus}>
        {!accessToken && (
          <Button cssProp={loginButton(theme)} onClick={handleClickLoginButton}>
            로그인
          </Button>
        )}
        {accessToken && (
          <>
            <Button cssProp={menu(theme)} onClick={handleClickMainButton}>
              <FiCalendar size={28} />
              <span css={menuTitle}>달력</span>
            </Button>
            <Button cssProp={menu(theme)} onClick={handleClickCategoryMenuButton}>
              <BiCategory size={28} />
              <span css={menuTitle}>카테고리</span>
            </Button>
            <Button cssProp={menu(theme)} onClick={handleClickProfileMenuButton}>
              <FaUserCircle size={28} />
              <span css={menuTitle}>프로필</span>
            </Button>
          </>
        )}
      </div>
    </div>
  );
}

export default NavBar;
