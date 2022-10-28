import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import { ValueOf } from '@/@types/util';

import { sideBarState } from '@/recoil/atoms';

import { PAGE_LAYOUT } from '@/constants/style';

import { pageLayout } from './PageLayout.styles';

interface PageLayoutProps {
  children: JSX.Element | JSX.Element[];
  type?: ValueOf<typeof PAGE_LAYOUT>;
}

function PageLayout({ children, type = PAGE_LAYOUT.DEFAULT }: PageLayoutProps) {
  const theme = useTheme();

  const isSideBarOpen = useRecoilValue(sideBarState);

  return <div css={pageLayout(theme, isSideBarOpen, type)}>{children}</div>;
}

export default PageLayout;
