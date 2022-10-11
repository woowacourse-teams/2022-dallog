import { useTheme } from '@emotion/react';
import { useRecoilValue } from 'recoil';

import { useDeleteSubscriptions } from '@/hooks/@queries/subscription';

import { CategoryType } from '@/@types/category';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';

import { CONFIRM_MESSAGE, TOOLTIP_MESSAGE } from '@/constants/message';

import { categoryItem, item, menuTitle, unsubscribeButton } from './SubscribedCategoryItem.styles';

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

  const user = useRecoilValue(userState);

  const { mutate } = useDeleteSubscriptions({ subscriptionId });

  const handleClickUnsubscribeButton = () => {
    if (window.confirm(CONFIRM_MESSAGE.UNSUBSCRIBE)) {
      mutate();
    }
  };

  const canUnsubscribeCategory = category.creator.id !== user.id;

  return (
    <div css={categoryItem} onClick={onClick}>
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
