import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import { sideBarState } from '@/recoil/atoms';

import Skeleton from '@/components/@common/Skeleton/Skeleton';

import { headerStyle, listStyle, skeletonStyle } from './FilterCategoryList.styles';

function FilterCategoryFallback() {
  const isSideBarOpen = useRecoilValue(sideBarState);
  const theme = useTheme();

  return (
    <div css={listStyle(theme, isSideBarOpen)}>
      <span css={headerStyle}> 구독 카테고리</span>
      <div css={skeletonStyle}>
        {new Array(10).fill(0).map((el, index) => (
          <Skeleton height="5rem" width="54rem" key={index} />
        ))}
      </div>
    </div>
  );
}

export default FilterCategoryFallback;
