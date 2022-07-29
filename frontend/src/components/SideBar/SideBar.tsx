import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import { sideBarState } from '@/recoil/atoms';

import FilterCategoryList from '@/components/FilterCategoryList/FilterCategoryList';
import MyCategoryList from '@/components/MyCategoryList/MyCategoryList';

import { sideBar } from './SideBar.styles';

function SideBar() {
  const isSideBarOpen = useRecoilValue(sideBarState);
  const theme = useTheme();

  return (
    <div css={sideBar(theme, isSideBarOpen)}>
      <FilterCategoryList />
      <MyCategoryList />
    </div>
  );
}

export default SideBar;
