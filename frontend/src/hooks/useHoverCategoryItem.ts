import { useState } from 'react';

import { EVENT } from '@/constants';

function useHoverCategoryItem() {
  const [hoveringPosY, setHoveringPosY] = useState<number | null>(null);

  const handleHoverCategoryItem = (e: React.MouseEvent) => {
    if (e.type === EVENT.MOUSE_ENTER) {
      setHoveringPosY(e.clientY);

      return;
    }

    setHoveringPosY(null);
  };

  return { hoveringPosY, handleHoverCategoryItem };
}

export default useHoverCategoryItem;
