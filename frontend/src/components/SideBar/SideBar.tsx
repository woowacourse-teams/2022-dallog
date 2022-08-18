import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import { sideBarState, userState } from '@/recoil/atoms';

import FilterCategoryList from '../FilterCategoryList/FilterCategoryList';
import { sideBar } from './SideBar.styles';

function SideBar() {
  const { accessToken } = useRecoilValue(userState);
  const isSideBarOpen = useRecoilValue(sideBarState);

  const theme = useTheme();

  if (!accessToken) {
    return <></>;
  }

  return (
    <div css={sideBar(theme, isSideBarOpen)}>
      <FilterCategoryList />
    </div>
  );
}

export default SideBar;
