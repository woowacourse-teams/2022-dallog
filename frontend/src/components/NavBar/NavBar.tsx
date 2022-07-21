import { useTheme } from '@emotion/react';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import { userState } from '@/atoms';

import Button from '@/components/@common/Button/Button';

import { PATH } from '@/constants';

import { BiCategory } from 'react-icons/bi';
import { FaUserCircle } from 'react-icons/fa';
import { FiCalendar } from 'react-icons/fi';
import { HiMenu } from 'react-icons/hi';

import { loginButton, menu, menus, menuTitle, navBar } from './NavBar.styles';

interface NavBarProps {
  openLoginModal?: () => void;
}

function NavBar({ openLoginModal }: NavBarProps) {
  const { accessToken } = useRecoilValue(userState);
  const theme = useTheme();
  const navigate = useNavigate();

  const handleClickMainButton = () => {
    navigate(PATH.MAIN);
  };

  const handleClickCategoryMenuButton = () => {
    navigate(PATH.CATEGORY);
  };

  const handleClickProfileMenuButton = () => {
    navigate(PATH.PROFILE);
  };

  return (
    <div css={navBar}>
      <div css={menus}>
        <Button cssProp={menu(theme)}>
          <HiMenu size={28} />
        </Button>
        <Button cssProp={menu(theme)} onClick={handleClickMainButton}>
          🌙&nbsp;&nbsp;달록
        </Button>
      </div>
      <div css={menus}>
        {!accessToken && (
          <Button cssProp={loginButton(theme)} onClick={openLoginModal}>
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
