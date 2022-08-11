import { AxiosError, AxiosResponse } from 'axios';
import { useInfiniteQuery, useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import useIntersect from '@/hooks/useIntersect';

import { CategoriesGetResponseType } from '@/@types/category';
import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import SubscribedCategoryItem from '@/components/SubscribedCategoryItem/SubscribedCategoryItem';
import UnsubscribedCategoryItem from '@/components/UnsubscribedCategoryItem/UnsubscribedCategoryItem';

import { API, CACHE_KEY } from '@/constants';

import categoryApi from '@/api/category';
import subscriptionApi from '@/api/subscription';

import {
  categoryTableHeaderStyle,
  categoryTableStyle,
  intersectTargetStyle,
  itemStyle,
} from './CategoryList.styles';

interface CategoryListProps {
  keyword: string;
}

function CategoryList({ keyword }: CategoryListProps) {
  const { accessToken } = useRecoilValue(userState);

  const {
    error: categoriesError,
    data: categoriesGetResponse,
    fetchNextPage,
    hasNextPage,
  } = useInfiniteQuery<AxiosResponse<CategoriesGetResponseType>, AxiosError>(
    [CACHE_KEY.CATEGORIES, keyword],
    ({ pageParam = 0 }) => categoryApi.getEntire(keyword, pageParam, API.CATEGORY_GET_SIZE),
    {
      getNextPageParam: ({ data }) => {
        if (data.categories.length > 0) {
          return data.page + 1;
        }
      },
    }
  );

  const { error: subscriptionsError, data: subscriptionsGetResponse } = useQuery<
    AxiosResponse<SubscriptionType[]>,
    AxiosError
  >(CACHE_KEY.SUBSCRIPTIONS, () => subscriptionApi.get(accessToken));

  const ref = useIntersect(() => {
    hasNextPage && fetchNextPage();
  });

  if (categoriesError || subscriptionsError) {
    return <>Error</>;
  }

  const categoryList = categoriesGetResponse?.pages.flatMap(({ data }) => data.categories);
  const subscriptionList = subscriptionsGetResponse?.data.map((el) => {
    return {
      subscriptionId: el.id,
      categoryId: el.category.id,
    };
  });

  return (
    <div>
      <div css={categoryTableHeaderStyle}>
        <span css={itemStyle}>생성 날짜</span>
        <span css={itemStyle}>카테고리 이름</span>
        <span css={itemStyle}>생성자</span>
        <span css={itemStyle}>구독 상태</span>
      </div>
      <div css={categoryTableStyle}>
        {categoryList?.map((category) => {
          const subscribedCategoryInfo = subscriptionList?.find(
            (el) => el.categoryId === category.id
          );

          if (subscribedCategoryInfo === undefined) {
            return <UnsubscribedCategoryItem key={category.id} category={category} />;
          }

          return (
            <SubscribedCategoryItem
              key={category.id}
              category={category}
              subscriptionId={subscribedCategoryInfo.subscriptionId}
            />
          );
        })}
        <div ref={ref} css={intersectTargetStyle} />
      </div>
    </div>
  );
}

export default CategoryList;
