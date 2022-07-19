import { useTheme } from '@emotion/react';
import { FcGoogle } from 'react-icons/fc';
import { useQuery } from 'react-query';

import Button from '@/components/@common/Button/Button';

import { CACHE_KEY } from '@/constants';

import loginApi from '@/api/login';

import { content, loginButton, loginModal, loginText, title } from './LoginModal.styles';

function LoginModal() {
  const theme = useTheme();

  const { error, refetch } = useQuery<string>(CACHE_KEY.ENTER, loginApi.getUrl, {
    enabled: false,
    onSuccess: (data) => onSuccessGetLoginUrl(data),
  });

  const onSuccessGetLoginUrl = (loginUrl: string) => {
    location.href = loginUrl;
  };

  const handleClickLoginModal = (e: React.MouseEvent) => {
    e.stopPropagation();
  };

  const handleClickGoogleLoginButton = () => {
    refetch();
  };

  if (error) {
    return <>Error</>;
  }

  return (
    <div css={loginModal} onClick={handleClickLoginModal}>
      <h1 css={title}>로그인</h1>
      <div css={content}>
        <Button cssProp={loginButton(theme)} onClick={handleClickGoogleLoginButton}>
          <FcGoogle size={18} />
          <span css={loginText}>Google 계정으로 계속하기</span>
        </Button>
      </div>
    </div>
  );
}

export default LoginModal;
