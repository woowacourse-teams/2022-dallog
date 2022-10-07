import { useTheme } from '@emotion/react';

import { usePostSubscription } from '@/hooks/@queries/subscription';

import { CategoryType } from '@/@types/category';

import Button from '@/components/@common/Button/Button';

import { PALETTE } from '@/constants/style';

import { getRandomNumber } from '@/utils';

import { categoryItem, item, subscribeButton } from './UnsubscribedCategoryItem.styles';

interface UnsubscribedCategoryItemProps {
  category: CategoryType;
  onClick: () => void;
}

function UnsubscribedCategoryItem({ category, onClick }: UnsubscribedCategoryItemProps) {
  const theme = useTheme();

  const body = {
    colorCode: PALETTE[getRandomNumber(0, PALETTE.length)],
  };

  const { mutate } = usePostSubscription({ categoryId: category.id });

  const handleClickSubscribeButton = () => {
    mutate(body);
  };

  return (
    <div css={categoryItem} onClick={onClick}>
      <span css={item}>{category.name}</span>
      <span css={item}>{category.creator.displayName}</span>
      <div css={item}>
        <Button cssProp={subscribeButton(theme)} onClick={handleClickSubscribeButton}>
          구독
        </Button>
      </div>
    </div>
  );
}

export default UnsubscribedCategoryItem;
