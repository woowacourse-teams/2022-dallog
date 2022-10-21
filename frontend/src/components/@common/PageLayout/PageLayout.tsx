import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import { sideBarState } from '@/recoil/atoms';

import { pageLayout } from './PageLayout.styles';

interface PageLayoutProps {
  children: JSX.Element | JSX.Element[];
}

function PageLayout({ children }: PageLayoutProps) {
  const theme = useTheme();

  const isSideBarOpen = useRecoilValue(sideBarState);

  return <div css={pageLayout(theme, isSideBarOpen)}>{children}</div>;
}

export default PageLayout;
