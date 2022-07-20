import { useTheme } from '@emotion/react';
import { useNavigate } from 'react-router-dom';
import { useRecoilState } from 'recoil';

import { userState } from '@/atoms';

import Button from '@/components/@common/Button/Button';

import { PATH } from '@/constants';

import { BiCategory } from 'react-icons/bi';
import { FaUserCircle } from 'react-icons/fa';

import { loginButton, menu, menus, menuTitle, navBar } from './NavBar.styles';

interface NavBarProps {
  openLoginModal?: () => void;
}

function NavBar({ openLoginModal }: NavBarProps) {
  const [user] = useRecoilState(userState);
  const theme = useTheme();
  const navigate = useNavigate();

  const handleClickLogoButton = () => {
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
      <Button cssProp={menu(theme)} onClick={handleClickLogoButton}>
        🌙&nbsp;&nbsp;달록
      </Button>
      <div css={menus}>
        {!user.accessToken && (
          <Button cssProp={loginButton(theme)} onClick={openLoginModal}>
            로그인
          </Button>
        )}
        {user.accessToken && (
          <>
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
