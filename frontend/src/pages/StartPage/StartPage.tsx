import useModal from '@/hooks/useModal';

import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import LoginModal from '@/components/LoginModal/LoginModal';
import PageLayout from '@/components/PageLayout/PageLayout';

function StartPage() {
  const { isOpen, openModal, closeModal } = useModal();

  return (
    <PageLayout openLoginModal={openModal}>
      <ModalPortal isOpen={isOpen} closeModal={closeModal}>
        <LoginModal />
      </ModalPortal>
    </PageLayout>
  );
}

export default StartPage;
