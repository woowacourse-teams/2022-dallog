import { AxiosError, AxiosResponse } from 'axios';
import { useInfiniteQuery } from 'react-query';

import { useGetSubscriptions } from '@/hooks/@queries/subscription';
import useIntersect from '@/hooks/useIntersect';

import { CategoriesGetResponseType } from '@/@types/category';

import SubscribedCategoryItem from '@/components/SubscribedCategoryItem/SubscribedCategoryItem';
import UnsubscribedCategoryItem from '@/components/UnsubscribedCategoryItem/UnsubscribedCategoryItem';

import { API, CACHE_KEY } from '@/constants/api';

import categoryApi from '@/api/category';

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
  const {
    error: categoriesGetError,
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

  const { error: subscriptionsGetError, data: subscriptionsGetResponse } = useGetSubscriptions({});

  const ref = useIntersect(() => {
    hasNextPage && fetchNextPage();
  });

  if (categoriesGetError || subscriptionsGetError) {
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
    <>
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
    </>
  );
}

export default CategoryList;
