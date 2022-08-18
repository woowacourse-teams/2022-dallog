import { useTheme } from '@emotion/react';

import Skeleton from '@/components/@common/Skeleton/Skeleton';

import { headerStyle, skeletonListStyle, skeletonStyle } from './FilterCategoryList.styles';

function FilterCategoryFallback() {
  const theme = useTheme();

  return (
    <div css={skeletonListStyle(theme, true)}>
      <span css={headerStyle}>구독 카테고리</span>
      <div css={skeletonStyle}>
        {new Array(10).fill(0).map((el, index) => (
          <Skeleton height="5rem" width="54rem" key={index} />
        ))}
      </div>
    </div>
  );
}

export default FilterCategoryFallback;
