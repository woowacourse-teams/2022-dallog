import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import LoginModal from '@/components/LoginModal/LoginModal';

interface StartPageProps {
  isLoginModalOpen: boolean;
  closeLoginModal: () => void;
}

function StartPage({ isLoginModalOpen, closeLoginModal }: StartPageProps) {
  return (
    <ModalPortal isOpen={isLoginModalOpen} closeModal={closeLoginModal}>
      <LoginModal />
    </ModalPortal>
  );
}

export default StartPage;
