import { Dispatch, SetStateAction } from 'react';

import { useGetEntireCategories } from '@/hooks/@queries/category';
import { useGetSubscriptions } from '@/hooks/@queries/subscription';
import useIntersect from '@/hooks/useIntersect';

import { CategoryType } from '@/@types/category';

import SubscribedCategoryItem from '@/components/SubscribedCategoryItem/SubscribedCategoryItem';
import UnsubscribedCategoryItem from '@/components/UnsubscribedCategoryItem/UnsubscribedCategoryItem';

import {
  categoryTableHeaderStyle,
  categoryTableStyle,
  intersectTargetStyle,
  itemStyle,
} from './CategoryList.styles';

interface CategoryListProps {
  keyword: string;
  setCategory: Dispatch<SetStateAction<Pick<CategoryType, 'id' | 'name'>>>;
}

function CategoryList({ keyword, setCategory }: CategoryListProps) {
  const {
    error: categoriesGetError,
    data: categoriesGetResponse,
    fetchNextPage,
    hasNextPage,
  } = useGetEntireCategories({ keyword });

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

  const handleClickCategoryItem = (category: Pick<CategoryType, 'id' | 'name'>) => {
    setCategory(category);
  };

  return (
    <>
      <div css={categoryTableHeaderStyle}>
        <span css={itemStyle}>제목</span>
        <span css={itemStyle}>개설자</span>
        <span css={itemStyle}>구독</span>
      </div>
      <div css={categoryTableStyle}>
        {categoryList?.map((category) => {
          const subscribedCategoryInfo = subscriptionList?.find(
            (el) => el.categoryId === category.id
          );

          if (subscribedCategoryInfo === undefined) {
            return (
              <UnsubscribedCategoryItem
                key={category.id}
                category={category}
                onClick={() => handleClickCategoryItem(category)}
              />
            );
          }

          return (
            <SubscribedCategoryItem
              key={category.id}
              category={category}
              subscriptionId={subscribedCategoryInfo.subscriptionId}
              onClick={() => handleClickCategoryItem(category)}
            />
          );
        })}
        <div ref={ref} css={intersectTargetStyle} />
      </div>
    </>
  );
}

export default CategoryList;
