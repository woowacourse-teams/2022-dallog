import { useTheme } from '@emotion/react';

import { useDeleteSubscriptions } from '@/hooks/@queries/subscription';
import useUserValue from '@/hooks/useUserValue';

import { CategoryType } from '@/@types/category';

import Button from '@/components/@common/Button/Button';

import { CONFIRM_MESSAGE, TOOLTIP_MESSAGE } from '@/constants/message';

import { getISODateString } from '@/utils/date';

import { categoryItem, item, menuTitle, unsubscribeButton } from './SubscribedCategoryItem.styles';

interface SubscribedCategoryItemProps {
  category: CategoryType;
  subscriptionId: number;
}

function SubscribedCategoryItem({ category, subscriptionId }: SubscribedCategoryItemProps) {
  const theme = useTheme();

  const { user } = useUserValue();

  const { mutate } = useDeleteSubscriptions({ subscriptionId });

  const handleClickUnsubscribeButton = () => {
    if (window.confirm(CONFIRM_MESSAGE.UNSUBSCRIBE)) {
      mutate();
    }
  };

  const canUnsubscribeCategory = category.creator.id !== user.id;

  return (
    <div css={categoryItem}>
      <span css={item}>{getISODateString(category.createdAt)}</span>
      <span css={item}>{category.name}</span>
      <span css={item}>{category.creator.displayName}</span>
      <div css={item}>
        <Button
          cssProp={unsubscribeButton(theme)}
          onClick={handleClickUnsubscribeButton}
          disabled={!canUnsubscribeCategory}
        >
          <p>구독중</p>
          {!canUnsubscribeCategory ? (
            <span css={menuTitle}>{TOOLTIP_MESSAGE.CANNOT_UNSUBSCRIBE_MINE}</span>
          ) : (
            <></>
          )}
        </Button>
      </div>
    </div>
  );
}

export default SubscribedCategoryItem;
