import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import { ValueOf } from '@/@types/util';

import { sideBarState } from '@/recoil/atoms';

import { PAGE_LAYOUT } from '@/constants/style';

import { pageLayout } from './PageLayout.styles';

interface PageLayoutProps {
  type?: ValueOf<typeof PAGE_LAYOUT>;
  children: JSX.Element | JSX.Element[];
}

function PageLayout({ type = PAGE_LAYOUT.DEFAULT, children }: PageLayoutProps) {
  const theme = useTheme();

  const isSideBarOpen = useRecoilValue(sideBarState);

  return <div css={pageLayout(theme, isSideBarOpen, type)}>{children}</div>;
}

export default PageLayout;
