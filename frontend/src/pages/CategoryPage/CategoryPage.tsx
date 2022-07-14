import { AxiosError, AxiosResponse } from 'axios';
import { useInfiniteQuery } from 'react-query';

import { CategoriesGetResponseType } from '@/@types/category';

import CategoryList from '@/components/CategoryList/CategoryList';
import PageLayout from '@/components/PageLayout/PageLayout';

import { API, CACHE_KEY } from '@/constants';

import categoryApi from '@/api/categories';

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
      <CategoryList
        categoryList={categoryList}
        getMoreCategories={fetchNextPage}
        hasNextPage={hasNextPage}
      />
    </PageLayout>
  );
}

export default CategoryPage;
