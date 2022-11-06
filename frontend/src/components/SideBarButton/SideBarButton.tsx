import { useRecoilState } from 'recoil';

import { sideBarSelector } from '@/recoil/selectors';

import theme from '@/styles/theme';

import Button from '@/components/@common/Button/Button';

import { MdMenu, MdMenuOpen } from 'react-icons/md';

import { menu, menuTitle } from './SideBarButton.styles';

function SideBarButton() {
  const [isSideBarOpen, toggleSideBarOpen] = useRecoilState(sideBarSelector);

  const handleClickSideBarButton = () => {
    toggleSideBarOpen(isSideBarOpen);
  };

  return (
    <Button
      cssProp={menu(theme)}
      onClick={handleClickSideBarButton}
      aria-label={isSideBarOpen ? '사이드바 닫기' : '사이드바 열기'}
    >
      {isSideBarOpen ? <MdMenuOpen size={28} /> : <MdMenu size={28} />}
      <span css={menuTitle}>메뉴</span>
    </Button>
  );
}

export default SideBarButton;
