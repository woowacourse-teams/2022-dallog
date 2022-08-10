import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import { sideBarState, userState } from '@/recoil/atoms';

import FilterCategoryList from '@/components/FilterCategoryList/FilterCategoryList';

import { sideBar } from './SideBar.styles';

function SideBar() {
  const theme = useTheme();

  const { accessToken } = useRecoilValue(userState);
  const isSideBarOpen = useRecoilValue(sideBarState);

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
