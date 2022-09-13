import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import useToggle from '@/hooks/useToggle';

import { SubscriptionType } from '@/@types/subscription';

import { sideBarState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import GoogleImportModal from '@/components/GoogleImportModal/GoogleImportModal';
import SideItem from '@/components/SideItem/SideItem';

import { AiOutlineDown, AiOutlineUp } from 'react-icons/ai';
import { BsPlus } from 'react-icons/bs';

import {
  contentStyle,
  headerLayoutStyle,
  headerStyle,
  listStyle,
  menuStyle,
  menuTitleStyle,
} from './SideGoogleList.styles';

interface SideGoogleListProps {
  categories: SubscriptionType[];
}

function SideGoogleList({ categories }: SideGoogleListProps) {
  const isSideBarOpen = useRecoilValue(sideBarState);

  const theme = useTheme();

  const { state: isGoogleListOpen, toggleState: toggleGoogleListOpen } = useToggle(true);
  const { state: isGoogleImportModalOpen, toggleState: toggleGoogleImportModalOpen } = useToggle();

  const handleClickGoogleImportButton = () => {
    toggleGoogleImportModalOpen();
  };

  return (
    <div css={listStyle(theme, isSideBarOpen)}>
      <div css={headerLayoutStyle}>
        <span css={headerStyle} onClick={toggleGoogleListOpen}>
          구글 카테고리
        </span>
        <Button cssProp={menuStyle}>
          <BsPlus size={20} onClick={handleClickGoogleImportButton} />
          <span css={menuTitleStyle}>구글 카테고리 추가</span>
        </Button>
        <Button onClick={toggleGoogleListOpen}>
          {isGoogleListOpen ? <AiOutlineUp /> : <AiOutlineDown />}
        </Button>
      </div>
      <div css={contentStyle(isGoogleListOpen, categories.length)}>
        {categories.map((el) => {
          return <SideItem key={el.category.id} subscription={el} />;
        })}
        {categories.length === 0 && <span>카테고리를 추가해주세요.</span>}
      </div>
      <ModalPortal isOpen={isGoogleImportModalOpen} closeModal={toggleGoogleImportModalOpen}>
        <GoogleImportModal closeModal={toggleGoogleImportModalOpen} />
      </ModalPortal>
    </div>
  );
}

export default SideGoogleList;
