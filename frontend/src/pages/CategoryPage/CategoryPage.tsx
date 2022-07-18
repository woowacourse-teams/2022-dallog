import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useInfiniteQuery } from 'react-query';

import { CategoriesGetResponseType } from '@/@types/category';

import FieldSet from '@/components/@common/FieldSet/FieldSet';
import CategoryList from '@/components/CategoryList/CategoryList';
import PageLayout from '@/components/PageLayout/PageLayout';

import { API, CACHE_KEY } from '@/constants';

import categoryApi from '@/api/category';

import { categoryNav, categoryPage, categorySearch } from './CategoryPage.styles';

function CategoryPage() {
  const {
    isLoading,
    error,
    data: categoriesGetResponse,
    fetchNextPage,
    hasNextPage,
  } = useInfiniteQuery<AxiosResponse<CategoriesGetResponseType>, AxiosError>(
    CACHE_KEY.CATEGORIES,
    ({ pageParam = 1 }) => categoryApi.get(pageParam, API.CATEGORY_GET_SIZE),
    {
      getNextPageParam: ({ data }) => {
        if (data.data.length) {
          return data.page + 1;
        }
      },
    }
  );

  if (isLoading || categoriesGetResponse === undefined) {
    return <div>Loading</div>;
  }

  if (error) {
    return <div>Error</div>;
  }

  const categoryList = categoriesGetResponse.pages.flatMap(({ data }) => data.data);

  return (
    <PageLayout>
      <div css={categoryPage}>
        <div css={categoryNav}>
          <FieldSet placeholder="카테고리 검색" cssProp={categorySearch} />
        </div>
        <CategoryList
          categoryList={categoryList}
          getMoreCategories={fetchNextPage}
          hasNextPage={hasNextPage}
        />
      </div>
    </PageLayout>
  );
}

export default CategoryPage;
