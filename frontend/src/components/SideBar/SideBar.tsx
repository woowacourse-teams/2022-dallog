import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import { sideBarState } from '@/recoil/atoms';

import FilterCategoryList from '@/components/FilterCategoryList/FilterCategoryList';

import { sideBar } from './SideBar.styles';

function SideBar() {
  const isSideBarOpen = useRecoilValue(sideBarState);
  const theme = useTheme();

  return (
    <div css={sideBar(theme, isSideBarOpen)}>
      <FilterCategoryList />
    </div>
  );
}

export default SideBar;
