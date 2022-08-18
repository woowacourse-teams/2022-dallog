import { css, Theme } from '@emotion/react';

const sideBar = ({ colors }: Theme) => css`
  overflow-y: overlay;
  overflow-x: hidden;
  position: fixed;
  z-index: 10;

  width: 64rem;
  height: calc(100vh - 16rem);
  padding: 4rem;
  border: 1px solid ${colors.GRAY_400};

  background: ${colors.WHITE};

  transition: width 0.3s;
`;

export { sideBar };
