import { useTheme } from '@emotion/react';

import { navBar } from './NavBar.styles';

function NavBar() {
  const theme = useTheme();

  return <div css={navBar(theme)}></div>;
}

export default NavBar;
