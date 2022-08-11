import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import { sideBarState } from '@/recoil/atoms';

import Skeleton from '@/components/@common/Skeleton/Skeleton';
import { itemStyle } from '@/components/FilterCategoryItem/FilterCategoryItem.styles';

import { contentStyle, headerStyle, listStyle } from './FilterCategoryList.styles';

function Fallback() {
  const isSideBarOpen = useRecoilValue(sideBarState);
  const theme = useTheme();

  return (
    <div css={listStyle(theme, isSideBarOpen)}>
      <span css={headerStyle}> 구독 카테고리</span>
      <div css={contentStyle}>
        {new Array(10).fill(0).map((el, index) => (
          <Skeleton cssProp={itemStyle(theme)} key={index} />
        ))}
      </div>
    </div>
  );
}

export default Fallback;
