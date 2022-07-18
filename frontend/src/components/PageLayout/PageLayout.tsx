import NavBar from '@/components/NavBar/NavBar';

import { content } from './PageLayout.styles';

interface PageLayoutProps {
  openLoginModal?: () => void;
  children: JSX.Element | JSX.Element[];
}

function PageLayout({ openLoginModal, children }: PageLayoutProps) {
  return (
    <>
      <NavBar openLoginModal={openLoginModal} />
      <div css={content}>{children}</div>
    </>
  );
}

export default PageLayout;
