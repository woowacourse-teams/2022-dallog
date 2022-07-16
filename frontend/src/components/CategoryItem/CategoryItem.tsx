import { CategoryType } from '@/@types/category';
import { useTheme } from '@emotion/react';

import { categoryItem } from './CategoryItem.styles';

interface CategoryItemProps {
  category: CategoryType;
}

function CategoryItem({ category }: CategoryItemProps) {
  const theme = useTheme();

  return (
    <div css={categoryItem(theme)}>
      <span>{category.createdAt.split('T')[0]}</span>
      <span>{category.name}</span>
    </div>
  );
}

export default CategoryItem;
