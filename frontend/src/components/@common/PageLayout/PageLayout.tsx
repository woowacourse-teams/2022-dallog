import { useRecoilValue } from 'recoil';

import { sideBarState } from '@/recoil/atoms';

import { pageLayout } from './PageLayout.styles';

interface PageLayoutProps {
  children: JSX.Element | JSX.Element[];
}

function PageLayout({ children }: PageLayoutProps) {
  const isSideBarOpen = useRecoilValue(sideBarState);

  return <div css={pageLayout(isSideBarOpen)}>{children}</div>;
}

export default PageLayout;
