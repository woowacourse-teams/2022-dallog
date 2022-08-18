import { useRecoilValue } from 'recoil';

import { sideBarState, userState } from '@/recoil/atoms';

import FilterCategoryList from '../FilterCategoryList/FilterCategoryList';
import { sideBar } from './SideBar.styles';

function SideBar() {
  const { accessToken } = useRecoilValue(userState);
  const isSideBarOpen = useRecoilValue(sideBarState);

  if (!accessToken || !isSideBarOpen) {
    return <></>;
  }

  return (
    <div css={sideBar}>
      <FilterCategoryList />
    </div>
  );
}

export default SideBar;
