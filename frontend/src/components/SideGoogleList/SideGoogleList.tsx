import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import useToggle from '@/hooks/useToggle';

import { SubscriptionType } from '@/@types/subscription';

import { sideBarState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import FilterCategoryItem from '@/components/FilterCategoryItem/FilterCategoryItem';
import GoogleImportModal from '@/components/GoogleImportModal/GoogleImportModal';

import { FcGoogle } from 'react-icons/fc';

import {
  contentStyle,
  googleImportButtonStyle,
  googleImportTextStyle,
  headerStyle,
  listStyle,
} from './SideGoogleList.styles';

interface SideGoogleListProps {
  categories: SubscriptionType[];
}

function SideGoogleList({ categories }: SideGoogleListProps) {
  const isSideBarOpen = useRecoilValue(sideBarState);

  const theme = useTheme();

  const { state: isGoogleImportModalOpen, toggleState: toggleGoogleImportModalOpen } = useToggle();

  const handleClickGoogleImportButton = () => {
    toggleGoogleImportModalOpen();
  };

  return (
    <div css={listStyle(theme, isSideBarOpen)}>
      <span css={headerStyle}>구글 카테고리 목록</span>
      <Button cssProp={googleImportButtonStyle(theme)} onClick={handleClickGoogleImportButton}>
        <FcGoogle size={20} />
        <p css={googleImportTextStyle}>구글 캘린더 가져오기</p>
      </Button>
      <div css={contentStyle}>
        {categories.map((el) => {
          return <FilterCategoryItem key={el.category.id} subscription={el} />;
        })}
      </div>
      <ModalPortal isOpen={isGoogleImportModalOpen} closeModal={toggleGoogleImportModalOpen}>
        <GoogleImportModal closeModal={toggleGoogleImportModalOpen} />
      </ModalPortal>
    </div>
  );
}

export default SideGoogleList;
