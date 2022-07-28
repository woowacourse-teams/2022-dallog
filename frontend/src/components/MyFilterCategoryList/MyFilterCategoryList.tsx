import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import { CACHE_KEY } from '@/constants';

import subscriptionApi from '@/api/subscription';

import FilterCategoryItem from '../FilterCategoryItem/FilterCategoryItem';
import { headerStyle } from './MyFilterCategoryList.styles';

function MyFilterCategoryList() {
  const { accessToken } = useRecoilValue(userState);

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
    <div>
      <span css={headerStyle}> 구독 카테고리</span>
      {categoryList.map((category) => {
        return <FilterCategoryItem key={category.id} category={category} />;
      })}
    </div>
  );
}

export default MyFilterCategoryList;
