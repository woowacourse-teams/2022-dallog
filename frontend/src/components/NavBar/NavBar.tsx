import { useTheme } from '@emotion/react';

import Button from '@/components/@common/Button/Button';

import { loginButton, navBar } from './NavBar.styles';

interface NavBarProps {
  openLoginModal?: () => void;
}

function NavBar({ openLoginModal }: NavBarProps) {
  const theme = useTheme();

  return (
    <div css={navBar(theme)}>
      <Button cssProp={loginButton} onClick={openLoginModal}>
        👤&nbsp;&nbsp;로그인
      </Button>
    </div>
  );
}

export default NavBar;
