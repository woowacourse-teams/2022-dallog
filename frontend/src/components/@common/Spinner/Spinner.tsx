import { useTheme } from '@emotion/react';

import { spinnerStyle } from './Spinner.styles';

interface SpinnerProps {
  size?: number;
}

function Spinner({ size = 5 }: SpinnerProps) {
  const theme = useTheme();

  return (
    <div css={spinnerStyle(theme, size)}>
      <div></div>
      <div></div>
      <div></div>
      <div></div>
    </div>
  );
}

export default Spinner;
