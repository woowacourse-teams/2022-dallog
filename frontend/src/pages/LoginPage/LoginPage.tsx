import { useTheme } from '@emotion/react';
import { useQuery } from 'react-query';

import Button from '@/components/@common/Button/Button';
import PageLayout from '@/components/@common/PageLayout/PageLayout';

import { CACHE_KEY } from '@/constants';

import loginApi from '@/api/login';

import { FaGithub } from 'react-icons/fa';
import { FcGoogle } from 'react-icons/fc';
import { RiKakaoTalkFill } from 'react-icons/ri';
import { SiNaver } from 'react-icons/si';

import {
  content,
  githubLoginButton,
  googleLoginButton,
  kakaoLoginButton,
  login,
  loginText,
  naverLoginButton,
  title,
} from './LoginPage.styles';

function LoginPage() {
  const theme = useTheme();

  const { error, refetch } = useQuery<string>(CACHE_KEY.ENTER, loginApi.getUrl, {
    enabled: false,
    onSuccess: (data) => onSuccessGetLoginUrl(data),
  });

  const onSuccessGetLoginUrl = (loginUrl: string) => {
    location.href = loginUrl;
  };

  const handleClickGoogleLoginButton = () => {
    refetch();
  };

  if (error) {
    return <>Error</>;
  }

  return (
    <PageLayout>
      <div css={login}>
        <h1 css={title}>로그인</h1>
        <div css={content}>
          <Button cssProp={naverLoginButton(theme)}>
            <SiNaver size={20} />
            <p css={loginText}>Naver 계정으로 계속하기</p>
          </Button>
          <Button cssProp={kakaoLoginButton(theme)}>
            <RiKakaoTalkFill size={20} />
            <p css={loginText}>Kakao 계정으로 계속하기</p>
          </Button>
          <Button cssProp={githubLoginButton(theme)}>
            <FaGithub size={20} />
            <p css={loginText}>Github 계정으로 계속하기</p>
          </Button>
          <Button cssProp={googleLoginButton(theme)} onClick={handleClickGoogleLoginButton}>
            <FcGoogle size={20} />
            <p css={loginText}>Google 계정으로 계속하기</p>
          </Button>
        </div>
      </div>
    </PageLayout>
  );
}

export default LoginPage;
