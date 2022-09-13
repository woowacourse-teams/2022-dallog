import { useState } from 'react';

import { ModalPosType } from '@/@types';

import useToggle from './useToggle';

function useModalPosition() {
  const [modalPos, setModalPos] = useState<ModalPosType>({});

  const { state, toggleState } = useToggle();

  const handleClickOpenButton = (e: React.MouseEvent) => {
    if (e.target !== e.currentTarget) {
      return;
    }

    setModalPos(calculateModalPos(e.clientX, e.clientY));
    toggleState();
  };

  const calculateModalPos = (clickX: number, clickY: number) => {
    const position = { top: clickY, right: 0, bottom: 0, left: clickX };

    if (clickX > innerWidth / 2) {
      position.right = innerWidth - clickX;
      position.left = 0;
    }

    if (clickY > innerHeight / 2) {
      position.bottom = innerHeight - clickY;
      position.top = 0;
    }

    return position;
  };

  return { state, toggleState, handleClickOpenButton, modalPos };
}

export default useModalPosition;
