import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import useToggle from '@/hooks/useToggle';

import { SubscriptionType } from '@/@types/subscription';

import { sideBarState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import SideItem from '@/components/SideItem/SideItem';

import { AiOutlineDown, AiOutlineUp } from 'react-icons/ai';

import {
  contentStyle,
  headerLayoutStyle,
  headerStyle,
  listStyle,
} from './SideSubscribedList.styles';

interface SideSubscribedListProps {
  categories: SubscriptionType[];
}

function SideSubscribedList({ categories }: SideSubscribedListProps) {
  const isSideBarOpen = useRecoilValue(sideBarState);

  const { state: isSubscribedListOpen, toggleState: toggleSubscribedListOpen } = useToggle(true);

  const theme = useTheme();

  return (
    <div css={listStyle(theme, isSideBarOpen)}>
      <div css={headerLayoutStyle}>
        <span css={headerStyle} onClick={toggleSubscribedListOpen}>
          구독 카테고리
        </span>
        <Button onClick={toggleSubscribedListOpen}>
          {isSubscribedListOpen ? <AiOutlineUp /> : <AiOutlineDown />}
        </Button>
      </div>
      <div css={contentStyle(isSubscribedListOpen, categories.length)}>
        {categories.map((el) => {
          return <SideItem key={el.category.id} subscription={el} />;
        })}
        {categories.length === 0 && <span>카테고리를 구독해주세요.</span>}
      </div>
    </div>
  );
}

export default SideSubscribedList;
