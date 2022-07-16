import Button from '@/components/@common/Button/Button';

import { content, googleLoginButton, loginModal, title } from './LoginModal.styles';

function LoginModal() {
  const handleClickLoginModal = (e: React.MouseEvent) => {
    e.stopPropagation();
  };

  return (
    <div css={loginModal} onClick={handleClickLoginModal}>
      <h1 css={title}>로그인</h1>
      <div css={content}>
        <Button cssProp={googleLoginButton}>Google 계정으로 계속하기</Button>
      </div>
    </div>
  );
}

export default LoginModal;
