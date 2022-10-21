import { css, Theme } from '@emotion/react';

const pageLayout = ({ mq }: Theme, isSideBarOpen: boolean) => css`
  overflow-y: auto;
  position: relative;

  height: calc(100vh - 16rem);
  margin-top: 16rem;

  ${mq?.laptop} {
    margin-left: ${isSideBarOpen ? '64rem' : '0'};

    transition: margin-left 0.3s;
  }
`;

export { pageLayout };
