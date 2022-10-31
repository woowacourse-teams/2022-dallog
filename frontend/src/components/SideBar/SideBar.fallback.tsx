import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import { sideBarState } from '@/recoil/atoms';

import Skeleton from '@/components/@common/Skeleton/Skeleton';

import { sideBar, skeletonItemStyle } from './SideBar.styles';

function SideBarFallback() {
  const theme = useTheme();

  const isSideBarOpen = useRecoilValue(sideBarState);

  return (
    <div css={sideBar(theme, isSideBarOpen)}>
      {new Array(3).fill(0).map((el, index) => (
        <section key={index}>
          <Skeleton cssProp={skeletonItemStyle} width="100%" height="7rem" />
          <Skeleton cssProp={skeletonItemStyle} width="70%" height="7rem" />
          <Skeleton cssProp={skeletonItemStyle} width="70%" height="7rem" />
          <Skeleton cssProp={skeletonItemStyle} width="70%" height="7rem" />
          <Skeleton cssProp={skeletonItemStyle} width="70%" height="7rem" />
          <Skeleton cssProp={skeletonItemStyle} width="70%" height="7rem" />
        </section>
      ))}
    </div>
  );
}

export default SideBarFallback;
