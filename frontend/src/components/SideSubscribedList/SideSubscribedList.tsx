import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import { SubscriptionType } from '@/@types/subscription';

import { sideBarState } from '@/recoil/atoms';

import FilterCategoryItem from '@/components/FilterCategoryItem/FilterCategoryItem';

import { contentStyle, headerStyle, listStyle } from './SideSubscribedList.styles';

interface SideSubscribedListProps {
  categories: SubscriptionType[];
}

function SideSubscribedList({ categories }: SideSubscribedListProps) {
  const isSideBarOpen = useRecoilValue(sideBarState);

  const theme = useTheme();

  return (
    <div css={listStyle(theme, isSideBarOpen)}>
      <span css={headerStyle}>구독 카테고리 목록</span>
      <div css={contentStyle}>
        {categories.map((el) => {
          return <FilterCategoryItem key={el.category.id} subscription={el} />;
        })}
      </div>
    </div>
  );
}

export default SideSubscribedList;
