import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import { useGetEditableCategories } from '@/hooks/@queries/category';
import { useGetSubscriptions } from '@/hooks/@queries/subscription';

import { sideBarState, userState } from '@/recoil/atoms';

import SideAdminList from '@/components/SideAdminList/SideAdminList';
import SideGoogleList from '@/components/SideGoogleList/SideGoogleList';
import SideSubscribedList from '@/components/SideSubscribedList/SideSubscribedList';

import { CATEGORY_TYPE } from '@/constants/category';

import { sideBar } from './SideBar.styles';

function SideBar() {
  const user = useRecoilValue(userState);
  const isSideBarOpen = useRecoilValue(sideBarState);

  const theme = useTheme();

  const { data: getEditableCategoriesResponse } = useGetEditableCategories({
    enabled: isSideBarOpen && !!user.accessToken,
  });

  const { data: getSubscriptionsResponse } = useGetSubscriptions({
    enabled: isSideBarOpen && !!user.accessToken,
  });

  const canEditCategories = getEditableCategoriesResponse?.data.map((category) => category.id);

  const adminList = getSubscriptionsResponse?.data.filter(
    (el) =>
      (canEditCategories?.includes(el.category.id) &&
        el.category.categoryType !== CATEGORY_TYPE.GOOGLE) ||
      el.category.categoryType === CATEGORY_TYPE.PERSONAL
  );

  const subscribedList = getSubscriptionsResponse?.data.filter(
    (el) =>
      !canEditCategories?.includes(el.category.id) &&
      el.category.categoryType === CATEGORY_TYPE.NORMAL
  );

  const googleList = getSubscriptionsResponse?.data.filter(
    (el) => el.category.categoryType === CATEGORY_TYPE.GOOGLE
  );

  return (
    <div css={sideBar(theme, isSideBarOpen)} tabIndex={10}>
      {adminList && <SideAdminList categories={adminList} />}
      {subscribedList && <SideSubscribedList categories={subscribedList} />}
      {googleList && <SideGoogleList categories={googleList} />}
    </div>
  );
}

export default SideBar;
