import { navBar } from './NavBar.styles';
import { useTheme } from '@emotion/react';

function NavBar() {
  const theme = useTheme();

  return <div css={navBar(theme)}></div>;
}

export default NavBar;
