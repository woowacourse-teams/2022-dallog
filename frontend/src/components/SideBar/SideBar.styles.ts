import { css, Theme } from '@emotion/react';

const sideBar = ({ colors }: Theme, isSideBarOpen: boolean) => css`
  overflow: hidden;
  position: fixed;
  z-index: 10;

  width: ${isSideBarOpen ? '64rem' : '0'};
  height: calc(100vh - 16rem);
  padding: ${isSideBarOpen ? '4rem' : '0'};
  border: 1px solid ${colors.GRAY_400};

  background: ${colors.WHITE};

  transition: width 0.3s;

  &:hover {
    overflow-y: overlay;
  }
`;

const skeletonItemStyle = css`
  margin: 3rem;
`;

export { skeletonItemStyle, sideBar };
