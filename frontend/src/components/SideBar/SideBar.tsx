import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import { SubscriptionType } from '@/@types/subscription';

import { sideBarState, userState } from '@/recoil/atoms';

import SideGoogleList from '@/components/SideGoogleList/SideGoogleList';
import SideMyList from '@/components/SideMyList/SideMyList';
import SideSubscribedList from '@/components/SideSubscribedList/SideSubscribedList';

import { CACHE_KEY } from '@/constants/api';
import { CATEGORY_TYPE } from '@/constants/category';

import subscriptionApi from '@/api/subscription';

import { sideBar } from './SideBar.styles';

function SideBar() {
  const user = useRecoilValue(userState);
  const isSideBarOpen = useRecoilValue(sideBarState);

  const theme = useTheme();

  const { isLoading, data } = useQuery<AxiosResponse<SubscriptionType[]>, AxiosError>(
    CACHE_KEY.SUBSCRIPTIONS,
    () => subscriptionApi.get(user.accessToken)
  );

  if (!user.accessToken || isLoading || data === undefined) {
    return <></>;
  }

  const subscribedList = data.data.filter(
    (el) => el.category.categoryType === CATEGORY_TYPE.NORMAL && el.category.creator.id !== user.id
  );

  const myList = data.data.filter(
    (el) => el.category.categoryType !== CATEGORY_TYPE.GOOGLE && el.category.creator.id === user.id
  );

  const googleList = data.data.filter((el) => el.category.categoryType === CATEGORY_TYPE.GOOGLE);

  return (
    <div css={sideBar(theme, isSideBarOpen)}>
      <SideMyList categories={myList} />
      <SideSubscribedList categories={subscribedList} />
      <SideGoogleList categories={googleList} />
    </div>
  );
}

export default SideBar;
