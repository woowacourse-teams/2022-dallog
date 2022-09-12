import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import useToggle from '@/hooks/useToggle';

import { SubscriptionType } from '@/@types/subscription';

import { sideBarState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import SideItem from '@/components/SideItem/SideItem';

import { AiOutlineDown, AiOutlineUp } from 'react-icons/ai';

import { contentStyle, headerLayoutStyle, headerStyle, listStyle } from './SideMyList.styles';

interface SideMyListProps {
  categories: SubscriptionType[];
}

function SideMyList({ categories }: SideMyListProps) {
  const isSideBarOpen = useRecoilValue(sideBarState);

  const theme = useTheme();

  const { state: isMyListOpen, toggleState: toggleMyListOpen } = useToggle(true);

  return (
    <div css={listStyle(theme, isSideBarOpen)}>
      <div css={headerLayoutStyle}>
        <span css={headerStyle} onClick={toggleMyListOpen}>
          나의 카테고리 목록
        </span>
        <Button onClick={toggleMyListOpen}>
          {isMyListOpen ? <AiOutlineUp /> : <AiOutlineDown />}
        </Button>
      </div>

      <div css={contentStyle(isMyListOpen, categories.length)}>
        {categories.map((el) => {
          return <SideItem key={el.category.id} subscription={el} />;
        })}
      </div>
    </div>
  );
}

export default SideMyList;
