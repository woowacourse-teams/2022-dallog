import { useEffect, useState } from 'react';

import { RESPONSIVE } from '@/constants/style';

import { debounce } from '@/utils';

function useRootFontSize() {
  const getRootFontSize = () => {
    if (innerWidth >= RESPONSIVE.LAPTOP.MIN_WIDTH) return RESPONSIVE.LAPTOP.FONT_SIZE;

    if (innerWidth > RESPONSIVE.MOBILE.MAX_WIDTH) return RESPONSIVE.TABLET.FONT_SIZE;

    return RESPONSIVE.MOBILE.FONT_SIZE;
  };

  const [rootFontSize, setRootFontSize] = useState(getRootFontSize());

  useEffect(() => {
    const handleResizeWindow = debounce(() => setRootFontSize(getRootFontSize()));

    window.addEventListener('resize', handleResizeWindow);

    return () => window.removeEventListener('resize', handleResizeWindow);
  }, []);

  return rootFontSize;
}

export default useRootFontSize;
