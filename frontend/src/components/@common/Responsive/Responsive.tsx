import { useTheme } from '@emotion/react';

import { layoutStyle } from './Responsive.styles';

interface ResponsiveProps {
  type: string;
  children: JSX.Element | JSX.Element[];
}

function Responsive({ type = 'all', children }: ResponsiveProps) {
  const theme = useTheme();

  return <div css={layoutStyle(theme, type)}>{children}</div>;
}

export default Responsive;
