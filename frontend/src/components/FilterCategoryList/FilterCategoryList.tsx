import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import { SubscriptionType } from '@/@types/subscription';

import { sideBarState, userState } from '@/recoil/atoms';

import { CACHE_KEY } from '@/constants';

import subscriptionApi from '@/api/subscription';

import FilterCategoryItem from '../FilterCategoryItem/FilterCategoryItem';
import { contentStyle, headerStyle, listStyle } from './FilterCategoryList.styles';

function FilterCategoryList() {
  const isSideBarOpen = useRecoilValue(sideBarState);
  const { accessToken } = useRecoilValue(userState);

  const theme = useTheme();

  const { data: subscriptionsGetResponse } = useQuery<
    AxiosResponse<SubscriptionType[]>,
    AxiosError
  >(CACHE_KEY.SUBSCRIPTIONS, () => subscriptionApi.get(accessToken));

  if (subscriptionsGetResponse === undefined) {
    return <div>Loading</div>;
  }

  const categoryList = subscriptionsGetResponse.data.map((el) => {
    return el.category;
  });

  return (
    <div css={listStyle(theme, isSideBarOpen)}>
      <span css={headerStyle}> 구독 카테고리</span>
      <div css={contentStyle}>
        {categoryList.map((category) => {
          return <FilterCategoryItem key={category.id} category={category} />;
        })}
      </div>
    </div>
  );
}

export default FilterCategoryList;
