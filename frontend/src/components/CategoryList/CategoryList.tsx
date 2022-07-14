import { useState } from 'react';
import { useTheme } from '@emotion/react';

import { CategoryType } from '@/@types';

import { categoryLayout } from './CategoryList.styles';

import CategoryItem from '@/components/CategoryItem/CategoryItem';

interface CategoryListProps {
  categories: CategoryType[];
}

function CategoryList({ categories }: CategoryListProps) {
  const theme = useTheme();

  return (
    <div>
      <div css={categoryLayout(theme)}>
        <span> 생성 날짜 </span>
        <span> 카테고리 이름 </span>
      </div>
      {categories.map((category) => (
        <CategoryItem key={category.id} category={category} />
      ))}
    </div>
  );
}

export default CategoryList;
