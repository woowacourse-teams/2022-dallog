import { css } from '@emotion/react';

const pageLayout = (isSideBarOpen: boolean) => css`
  overflow-y: auto;

  height: calc(100vh - 16rem);
  margin-top: 16rem;
  margin-left: ${isSideBarOpen ? '64rem' : '0'};

  transition: margin-left 0.3s;
`;

export { pageLayout };
