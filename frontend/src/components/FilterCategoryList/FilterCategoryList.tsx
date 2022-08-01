import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import { SubscriptionType } from '@/@types/subscription';

import { sideBarState, userState } from '@/recoil/atoms';

import FilterCategoryItem from '@/components/FilterCategoryItem/FilterCategoryItem';

import { CACHE_KEY } from '@/constants';

import subscriptionApi from '@/api/subscription';

import { contentStyle, headerStyle, listStyle } from './FilterCategoryList.styles';

function FilterCategoryList() {
  const isSideBarOpen = useRecoilValue(sideBarState);
  const { accessToken } = useRecoilValue(userState);

  const theme = useTheme();

  const { data } = useQuery<AxiosResponse<SubscriptionType[]>, AxiosError>(
    CACHE_KEY.SUBSCRIPTIONS,
    () => subscriptionApi.get(accessToken)
  );

  if (data === undefined) {
    return <div>Loading</div>;
  }

  return (
    <div css={listStyle(theme, isSideBarOpen)}>
      <span css={headerStyle}> 구독 카테고리</span>
      <div css={contentStyle}>
        {data.data.map((el) => {
          return <FilterCategoryItem key={el.category.id} subscription={el} />;
        })}
      </div>
    </div>
  );
}

export default FilterCategoryList;
