import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import useRootFontSize from '@/hooks/useRootFontSize';
import useToggle from '@/hooks/useToggle';

import { SubscriptionType } from '@/@types/subscription';

import { sideBarState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import CategoryAddModal from '@/components/CategoryAddModal/CategoryAddModal';
import SideItem from '@/components/SideItem/SideItem';

import { MdAdd, MdKeyboardArrowDown, MdKeyboardArrowUp } from 'react-icons/md';

import {
  contentStyle,
  headerLayoutStyle,
  headerStyle,
  listStyle,
  menuStyle,
  menuTitleStyle,
} from './SideAdminList.styles';

interface SideAdminListProps {
  categories: SubscriptionType[];
}

function SideAdminList({ categories }: SideAdminListProps) {
  const isSideBarOpen = useRecoilValue(sideBarState);

  const theme = useTheme();

  const rootFontSize = useRootFontSize();

  const { state: isMyListOpen, toggleState: toggleMyListOpen } = useToggle(true);

  const { state: isCategoryAddModalOpen, toggleState: toggleCategoryAddModalOpen } = useToggle();

  const handleClickCategoryAddButton = () => {
    toggleCategoryAddModalOpen();
  };

  return (
    <div css={listStyle(theme, isSideBarOpen)}>
      <div css={headerLayoutStyle}>
        <span css={headerStyle} onClick={toggleMyListOpen}>
          관리 카테고리
        </span>
        <Button cssProp={menuStyle}>
          <MdAdd size={rootFontSize * 5} onClick={handleClickCategoryAddButton} />
          <span css={menuTitleStyle}>카테고리 추가</span>
        </Button>
        <Button onClick={toggleMyListOpen}>
          {isMyListOpen ? (
            <MdKeyboardArrowUp size={rootFontSize * 5} />
          ) : (
            <MdKeyboardArrowDown size={rootFontSize * 5} />
          )}
        </Button>
      </div>

      <div css={contentStyle(theme, isMyListOpen, categories.length)}>
        {categories.map((el) => {
          return <SideItem key={el.category.id} subscription={el} />;
        })}
      </div>
      <ModalPortal isOpen={isCategoryAddModalOpen} closeModal={toggleCategoryAddModalOpen}>
        <CategoryAddModal closeModal={toggleCategoryAddModalOpen} />
      </ModalPortal>
    </div>
  );
}

export default SideAdminList;
