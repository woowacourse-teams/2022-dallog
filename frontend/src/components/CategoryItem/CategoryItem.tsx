import { useTheme } from '@emotion/react';

import { CategoryType } from '@/@types/category';

import SubscribeButton from '@/components/SubscribeButton/SubscribeButton';

import { categoryItem, item } from './CategoryItem.styles';

interface CategoryItemProps {
  category: CategoryType;
  isSubscribing: boolean;
}

function CategoryItem({ category, isSubscribing }: CategoryItemProps) {
  const theme = useTheme();

  return (
    <div css={categoryItem(theme)}>
      <span css={item}>{category.createdAt.split('T')[0]}</span>
      <span css={item}>{category.name}</span>
      <div css={item}>
        <SubscribeButton isSubscribing={isSubscribing}></SubscribeButton>
      </div>
    </div>
  );
}

export default CategoryItem;
