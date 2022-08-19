import { useSetRecoilState } from 'recoil';

import { snackBarState } from '@/recoil/atoms';

function useSnackBar() {
  const setText = useSetRecoilState(snackBarState);

  const openSnackBar = (text: string) => {
    setText({ text });
  };

  return { openSnackBar };
}

export default useSnackBar;
