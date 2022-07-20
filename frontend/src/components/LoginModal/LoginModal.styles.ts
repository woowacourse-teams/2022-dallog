import { css, Theme } from '@emotion/react';

const loginModal = ({ colors, flex }: Theme) => css`
  ${flex.column}

  width: 105rem;
  height: 140rem;
  padding: 12.5rem;
  border-radius: 12px;

  background: ${colors.WHITE};
`;

const title = ({ colors }: Theme) => css`
  font-size: 8rem;
  font-weight: bold;
  color: ${colors.GRAY_700};
`;

const content = ({ flex }: Theme) => css`
  ${flex.column}

  gap: 3rem;

  height: 100%;
`;

const loginButton = ({ colors, flex }: Theme) => css`
  ${flex.row}

  position: relative;
  justify-content: flex-start;

  width: 75rem;
  height: 15rem;
  padding: 4rem;
  border-radius: 8px;
  box-shadow: 2px 2px 2px ${colors.GRAY_400};

  font-size: 4rem;
  font-weight: 500;
  color: ${colors.WHITE};

  &:hover {
    box-shadow: 3px 3px 3px ${colors.GRAY_500};
  }
`;

const googleLoginButton = ({ colors, flex }: Theme) => css`
  ${loginButton({ colors, flex })}

  border: 1px solid ${colors.GRAY_400};

  background: ${colors.WHITE};

  color: ${colors.BLACK}8a;
`;

const naverLoginButton = ({ colors, flex }: Theme) => css`
  ${loginButton({ colors, flex })}

  background: ${colors.GREEN_500};
`;

const kakaoLoginButton = ({ colors, flex }: Theme) => css`
  ${loginButton({ colors, flex })}

  background: ${colors.YELLOW_400};

  color: ${colors.BLACK}8a;
`;

const githubLoginButton = ({ colors, flex }: Theme) => css`
  ${loginButton({ colors, flex })}

  background: ${colors.BLACK};
`;

const loginText = css`
  width: 100%;
`;

export {
  content,
  githubLoginButton,
  googleLoginButton,
  kakaoLoginButton,
  loginButton,
  loginModal,
  loginText,
  naverLoginButton,
  title,
};
