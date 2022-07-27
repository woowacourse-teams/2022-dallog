import { useTheme } from '@emotion/react';
import { useEffect, useState } from 'react';
import { useRecoilValue } from 'recoil';

import { snackBarState } from '@/recoil/atoms';

import { snackBarStyle } from './SnackBar.styles';

function SnackBar() {
  const theme = useTheme();

  const snackBarInfo = useRecoilValue(snackBarState);

  const [timer, setTimer] = useState<null | NodeJS.Timeout>(null);

  useEffect(() => {
    if (snackBarInfo.text === '' || timer) {
      return;
    }

    const newTimer = setTimeout(() => {
      setTimer(null);
    }, 1500);

    setTimer(newTimer);
  }, [snackBarInfo]);

  const isOpen = timer !== null;

  return <div css={snackBarStyle(theme, isOpen)}>{snackBarInfo.text}</div>;
}

export default SnackBar;
