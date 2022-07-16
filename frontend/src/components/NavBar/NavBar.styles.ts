import { css, Theme } from '@emotion/react';

const navBar = ({ colors, flex }: Theme) => css`
  ${flex.row}

  position: fixed;
  top: 0;
  left: 0;

  width: 100%;
  height: 16rem;
  padding: 2rem;

  background: ${colors.WHITE};

  box-shadow: 0 4px 4px rgba(0, 0, 0, 0.25);
`;

const loginButton = ({ colors }: Theme) => css`
  background: transparent;

  font-size: 5rem;
  font-weight: bold;
  color: ${colors.GRAY_700};
`;

export { loginButton, navBar };
