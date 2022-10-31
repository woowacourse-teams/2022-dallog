import { css, Theme } from '@emotion/react';

import { ValueOf } from '@/@types/util';

import { PAGE_LAYOUT } from '@/constants/style';

const pageLayout = (
  { mq }: Theme,
  isSideBarOpen: boolean,
  type: ValueOf<typeof PAGE_LAYOUT>
) => css`
  overflow-y: auto;
  position: relative;

  height: calc(100vh - 16rem);
  margin-top: 16rem;

  ${mq?.laptop} {
    margin-left: ${type === PAGE_LAYOUT.DEFAULT ? '0' : isSideBarOpen ? '64rem' : '0'};

    transition: margin-left 0.3s;
  }
`;

export { pageLayout };
