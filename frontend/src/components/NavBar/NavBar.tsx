import { useTheme } from '@emotion/react';
import { useRecoilState } from 'recoil';

import { userState } from '@/atoms';

import Button from '@/components/@common/Button/Button';

import { BiCategory } from 'react-icons/bi';
import { FaUserCircle } from 'react-icons/fa';

import { loginButton, menu, menus, menuTitle, navBar } from './NavBar.styles';

interface NavBarProps {
  openLoginModal?: () => void;
}

function NavBar({ openLoginModal }: NavBarProps) {
  const [user] = useRecoilState(userState);
  const theme = useTheme();

  return (
    <div css={navBar}>
      <Button cssProp={menu(theme)}>🌙&nbsp;&nbsp;달록</Button>
      <div css={menus}>
        {!user.accessToken && (
          <Button cssProp={loginButton(theme)} onClick={openLoginModal}>
            로그인
          </Button>
        )}
        {user.accessToken && (
          <>
            <Button cssProp={menu(theme)} onClick={openLoginModal}>
              <BiCategory size={28} />
              <span css={menuTitle}>카테고리</span>
            </Button>
            <Button cssProp={menu(theme)} onClick={openLoginModal}>
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
