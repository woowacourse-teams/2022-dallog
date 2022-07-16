import { AxiosError, AxiosResponse } from 'axios';
import { FetchNextPageOptions, InfiniteQueryObserverResult } from 'react-query';

import useIntersect from '@/hooks/useIntersect';

import { CategoriesGetResponseType, CategoryType } from '@/@types/category';

import CategoryItem from '@/components/CategoryItem/CategoryItem';

import { categoryTable, categoryTableHeader, intersectTarget } from './CategoryList.styles';

interface CategoryListProps {
  categoryList: CategoryType[];
  getMoreCategories: (
    options?: FetchNextPageOptions | undefined
  ) => Promise<InfiniteQueryObserverResult<AxiosResponse<CategoriesGetResponseType>, AxiosError>>;
  hasNextPage: boolean | undefined;
}

function CategoryList({ categoryList, getMoreCategories, hasNextPage }: CategoryListProps) {
  const ref = useIntersect(() => {
    hasNextPage && getMoreCategories();
  });

  return (
    <div css={categoryTable}>
      <div css={categoryTableHeader}>
        <span> 생성 날짜 </span>
        <span> 카테고리 이름 </span>
      </div>
      {categoryList.map((category) => (
        <CategoryItem key={category.id} category={category} />
      ))}
      <div ref={ref} css={intersectTarget}></div>
    </div>
  );
}

export default CategoryList;
