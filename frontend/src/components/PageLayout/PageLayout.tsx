import NavBar from '@/components/NavBar/NavBar';

import { content } from './PageLayout.styles';

interface PageLayoutProps {
  children: JSX.Element | JSX.Element[];
}

function PageLayout({ children }: PageLayoutProps) {
  return (
    <>
      <NavBar />
      <div css={content}>{children}</div>
    </>
  );
}

export default PageLayout;
