import { useTheme } from '@emotion/react';

import { useGetEditableCategories, useGetSingleCategory } from '@/hooks/@queries/category';
import { useDeleteSubscriptions } from '@/hooks/@queries/subscription';
import useHoverCategoryItem from '@/hooks/useHoverCategoryItem';

import { CategoryType } from '@/@types/category';

import Button from '@/components/@common/Button/Button';

import { CONFIRM_MESSAGE } from '@/constants/message';

import { getISODateString } from '@/utils/date';

import {
  categoryItem,
  detailStyle,
  item,
  unsubscribeButton,
} from './SubscribedCategoryItem.styles';

interface SubscribedCategoryItemProps {
  category: CategoryType;
  subscriptionId: number;
  onClick: () => void;
}

function SubscribedCategoryItem({
  category,
  subscriptionId,
  onClick,
}: SubscribedCategoryItemProps) {
  const theme = useTheme();

  const { hoveringPosY, handleHoverCategoryItem } = useHoverCategoryItem();

  const { data: getSingleCategoryResponse } = useGetSingleCategory({
    categoryId: category.id,
    enabled: !!hoveringPosY,
  });

  const { data: getEditableCategoriesResponse } = useGetEditableCategories({});

  const { mutate } = useDeleteSubscriptions({ subscriptionId });

  const handleClickUnsubscribeButton = () => {
    if (window.confirm(CONFIRM_MESSAGE.UNSUBSCRIBE)) {
      mutate();
    }
  };

  const canUnsubscribeCategory = !getEditableCategoriesResponse?.data.some(
    (el) => el.id === category.id
  );

  return (
    <div
      css={categoryItem}
      onClick={onClick}
      onMouseEnter={handleHoverCategoryItem}
      onMouseLeave={handleHoverCategoryItem}
    >
      <span css={item}>{category.name}</span>
      <span css={item}>{category.creator.displayName}</span>
      <div css={item}>
        <Button
          cssProp={unsubscribeButton(theme)}
          onClick={handleClickUnsubscribeButton}
          disabled={!canUnsubscribeCategory}
        >
          구독중
        </Button>
      </div>
      {hoveringPosY !== null && (
        <div css={detailStyle(theme, hoveringPosY < innerHeight / 2)}>
          {`구독자 ${
            getSingleCategoryResponse?.data.subscriberCount ?? '-'
          }명 • 개설일 ${getISODateString(category.createdAt)}`}
        </div>
      )}
    </div>
  );
}

export default SubscribedCategoryItem;
