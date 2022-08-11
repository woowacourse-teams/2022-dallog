import { useTheme } from '@emotion/react';
import React, { Suspense } from 'react';
import { useRecoilValue } from 'recoil';

import { sideBarState, userState } from '@/recoil/atoms';

import Fallback from '@/components/FilterCategoryList/FilterCategoryList.fallback';

import { sideBar } from './SideBar.styles';

const FilterCategoryList = React.lazy(
  () => import('@/components/FilterCategoryList/FilterCategoryList')
);

function SideBar() {
  const theme = useTheme();

  const { accessToken } = useRecoilValue(userState);
  const isSideBarOpen = useRecoilValue(sideBarState);

  if (!accessToken) {
    return <></>;
  }

  return (
    <div css={sideBar(theme, isSideBarOpen)}>
      <Suspense fallback={<Fallback />}>
        <FilterCategoryList />
      </Suspense>
    </div>
  );
}

export default SideBar;
