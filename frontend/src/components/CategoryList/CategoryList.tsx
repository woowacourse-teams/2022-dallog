import { AxiosError, AxiosResponse } from 'axios';
import { FetchNextPageOptions, InfiniteQueryObserverResult, useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import useIntersect from '@/hooks/useIntersect';

import { CategoriesGetResponseType, CategoryType } from '@/@types/category';
import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/atoms';

import CategoryItem from '@/components/CategoryItem/CategoryItem';

import { CACHE_KEY } from '@/constants';

import subscriptionApi from '@/api/subscription';

import { categoryTable, categoryTableHeader, intersectTarget, item } from './CategoryList.styles';

interface CategoryListProps {
  categoryList: CategoryType[];
  getMoreCategories: (
    options?: FetchNextPageOptions | undefined
  ) => Promise<InfiniteQueryObserverResult<AxiosResponse<CategoriesGetResponseType>, AxiosError>>;
  hasNextPage: boolean | undefined;
}

function CategoryList({ categoryList, getMoreCategories, hasNextPage }: CategoryListProps) {
  const { accessToken } = useRecoilValue(userState);

  const { data: subscriptionsGetResponse, refetch: refetchSubscriptions } = useQuery<
    AxiosResponse<SubscriptionType[]>,
    AxiosError
  >(CACHE_KEY.SUBSCRIPTIONS, () => subscriptionApi.get(accessToken));

  const ref = useIntersect(() => {
    hasNextPage && getMoreCategories();
  });

  if (subscriptionsGetResponse === undefined) {
    return <div></div>;
  }

  const subscriptions = subscriptionsGetResponse.data.map((el) => el.category.id);

  return (
    <div css={categoryTable}>
      <div css={categoryTableHeader}>
        <span css={item}> 생성 날짜 </span>
        <span css={item}> 카테고리 이름 </span>
        <span css={item}> 구독 상태 </span>
      </div>
      {categoryList.map((category) => {
        const isSubscribing = subscriptions.includes(category.id);

        return (
          <CategoryItem
            key={category.id}
            category={category}
            isSubscribing={isSubscribing}
            refetchSubscriptions={refetchSubscriptions}
          />
        );
      })}
      <div ref={ref} css={intersectTarget}></div>
    </div>
  );
}

export default CategoryList;
