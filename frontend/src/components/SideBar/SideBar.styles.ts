import { css, Theme } from '@emotion/react';

const sideBar = ({ colors, flex }: Theme, isSideBarOpen: boolean) => css`
  ${flex.col}

  align-items: flex-start;
  overflow: overlay;
  position: fixed;
  z-index: 10;

  width: ${isSideBarOpen ? '64rem' : '0'};
  height: calc(100vh - 16rem);
  padding: ${isSideBarOpen ? '5rem' : '0'};
  border: 1px solid ${colors.GRAY_400};

  background: ${colors.WHITE};

  transition: width 0.3s;
`;

export { sideBar };
