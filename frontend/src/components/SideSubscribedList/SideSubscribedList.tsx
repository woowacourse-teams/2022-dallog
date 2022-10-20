import { useTheme } from '@emotion/react';
import { useNavigate } from 'react-router-dom';
import { useRecoilValue } from 'recoil';

import useToggle from '@/hooks/useToggle';

import { SubscriptionType } from '@/@types/subscription';

import { sideBarState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import SideItem from '@/components/SideItem/SideItem';

import { PATH } from '@/constants';

import { getRootFontSize } from '@/utils';

import { MdAdd, MdKeyboardArrowDown, MdKeyboardArrowUp } from 'react-icons/md';

import {
  contentStyle,
  headerLayoutStyle,
  headerStyle,
  listStyle,
  menuStyle,
  menuTitleStyle,
} from './SideSubscribedList.styles';

interface SideSubscribedListProps {
  categories: SubscriptionType[];
}

function SideSubscribedList({ categories }: SideSubscribedListProps) {
  const isSideBarOpen = useRecoilValue(sideBarState);

  const { state: isSubscribedListOpen, toggleState: toggleSubscribedListOpen } = useToggle(true);

  const theme = useTheme();

  const navigate = useNavigate();

  const handleClickCategoryAddButton = () => navigate(PATH.CATEGORY);

  const rootFontSize = getRootFontSize();

  return (
    <div css={listStyle(theme, isSideBarOpen)}>
      <div css={headerLayoutStyle}>
        <span css={headerStyle} onClick={toggleSubscribedListOpen}>
          구독 카테고리
        </span>
        <Button cssProp={menuStyle}>
          <MdAdd size={rootFontSize * 5} onClick={handleClickCategoryAddButton} />
          <span css={menuTitleStyle}>카테고리 구독</span>
        </Button>
        <Button onClick={toggleSubscribedListOpen}>
          {isSubscribedListOpen ? (
            <MdKeyboardArrowUp size={rootFontSize * 5} />
          ) : (
            <MdKeyboardArrowDown size={rootFontSize * 5} />
          )}
        </Button>
      </div>
      <div
        css={contentStyle(
          theme,
          isSubscribedListOpen,
          categories.length === 0 ? 1 : categories.length
        )}
      >
        {categories.map((el) => {
          return <SideItem key={el.category.id} subscription={el} />;
        })}
        {categories.length === 0 && <span>카테고리를 구독해주세요.</span>}
      </div>
    </div>
  );
}

export default SideSubscribedList;
