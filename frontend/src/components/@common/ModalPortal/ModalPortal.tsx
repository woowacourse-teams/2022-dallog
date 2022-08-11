import { useTheme } from '@emotion/react';
import ReactDOM from 'react-dom';

import { TRANSPARENT } from '@/constants/style';

import { dimmer } from './ModalPortal.styles';

interface ModalPortalProps {
  isOpen: boolean;
  closeModal: () => void;
  children: JSX.Element | JSX.Element[];
  dimmerBackground?: typeof TRANSPARENT;
}

function ModalPortal({ isOpen, closeModal, children, dimmerBackground }: ModalPortalProps) {
  const modalElement = document.getElementById('modal');

  const theme = useTheme();

  if (!(modalElement instanceof HTMLElement)) {
    return <></>;
  }

  const handleClickDimmer = (e: React.MouseEvent) => {
    if (e.target !== e.currentTarget) {
      return;
    }

    closeModal();
  };

  const element = isOpen && (
    <div css={dimmer(theme, isOpen, dimmerBackground)} onClick={handleClickDimmer}>
      {children}
    </div>
  );

  return ReactDOM.createPortal(element, modalElement);
}

export default ModalPortal;
