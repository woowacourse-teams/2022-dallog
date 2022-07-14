import { CategoryType } from '@/@types';
import { useTheme } from '@emotion/react';

import { categoryLayout } from './CategoryItem.styles';

interface CategoryProps {
  category: CategoryType;
}

function CategoryItem({ category }: CategoryProps) {
  const theme = useTheme();

  return (
    <div css={categoryLayout(theme)}>
      <span>{category.createdAt.split('T')[0]}</span>
      <span>{category.name}</span>
    </div>
  );
}

export default CategoryItem;
