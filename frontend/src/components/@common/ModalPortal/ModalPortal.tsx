import { SerializedStyles, useTheme } from '@emotion/react';
import ReactDOM from 'react-dom';

import { dimmer } from './ModalPortal.styles';

interface ModalPortalProps {
  isOpen: boolean;
  closeModal: () => void;
  children: JSX.Element | JSX.Element[];
  dimmerBackground?: string;
}

function ModalPortal({ isOpen, closeModal, children, dimmerBackground }: ModalPortalProps) {
  const modalElement = document.getElementById('modal');

  const theme = useTheme();

  if (!(modalElement instanceof HTMLElement)) {
    return <></>;
  }

  const element = isOpen && (
    <div css={dimmer(theme, isOpen, dimmerBackground)} onClick={closeModal}>
      {children}
    </div>
  );

  return ReactDOM.createPortal(element, modalElement);
}

export default ModalPortal;
