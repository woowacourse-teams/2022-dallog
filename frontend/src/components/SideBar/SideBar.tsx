import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import { useGetEditableCategories } from '@/hooks/@queries/category';
import { useGetSubscriptions } from '@/hooks/@queries/subscription';

import { sideBarState, userState } from '@/recoil/atoms';

import Spinner from '@/components/@common/Spinner/Spinner';
import SideAdminList from '@/components/SideAdminList/SideAdminList';
import SideGoogleList from '@/components/SideGoogleList/SideGoogleList';
import SideSubscribedList from '@/components/SideSubscribedList/SideSubscribedList';

import { CATEGORY_TYPE } from '@/constants/category';

import { sideBar } from './SideBar.styles';

function SideBar() {
  const user = useRecoilValue(userState);
  const isSideBarOpen = useRecoilValue(sideBarState);

  const theme = useTheme();

  const { isLoading: isGetEditableCategoriesLoading, data: getEditableCategoriesResponse } =
    useGetEditableCategories({
      enabled: isSideBarOpen && !!user.accessToken,
    });

  const { isLoading: isGetSubscriptionsLoading, data: getSubscriptionsResponse } =
    useGetSubscriptions({
      enabled: isSideBarOpen && !!user.accessToken,
    });

  if (
    !user.accessToken ||
    isGetEditableCategoriesLoading ||
    !getEditableCategoriesResponse ||
    isGetSubscriptionsLoading ||
    !getSubscriptionsResponse
  ) {
    return (
      <div css={sideBar(theme, isSideBarOpen)}>
        <Spinner size={10} />
      </div>
    );
  }

  const canEditCategories = getEditableCategoriesResponse.data.map((category) => category.id);

  const adminList = getSubscriptionsResponse.data.filter(
    (el) =>
      (canEditCategories.includes(el.category.id) &&
        el.category.categoryType !== CATEGORY_TYPE.GOOGLE) ||
      el.category.categoryType === CATEGORY_TYPE.PERSONAL
  );

  const subscribedList = getSubscriptionsResponse.data.filter(
    (el) =>
      !canEditCategories.includes(el.category.id) &&
      el.category.categoryType === CATEGORY_TYPE.NORMAL
  );

  const googleList = getSubscriptionsResponse.data.filter(
    (el) => el.category.categoryType === CATEGORY_TYPE.GOOGLE
  );

  return (
    <div css={sideBar(theme, isSideBarOpen)} tabIndex={10}>
      <SideAdminList categories={adminList} />
      <SideSubscribedList categories={subscribedList} />
      <SideGoogleList categories={googleList} />
    </div>
  );
}

export default SideBar;
