import { useTheme } from '@emotion/react';

import { useGetSingleCategory } from '@/hooks/@queries/category';
import { usePostSubscription } from '@/hooks/@queries/subscription';
import useHoverCategoryItem from '@/hooks/useHoverCategoryItem';

import { CategoryType } from '@/@types/category';

import Button from '@/components/@common/Button/Button';

import { PALETTE } from '@/constants/style';

import { getRandomNumber } from '@/utils';
import { getISODateString } from '@/utils/date';

import {
  categoryItem,
  detailStyle,
  item,
  subscribeButton,
} from './UnsubscribedCategoryItem.styles';

interface UnsubscribedCategoryItemProps {
  category: CategoryType;
  onClick: () => void;
}

function UnsubscribedCategoryItem({ category, onClick }: UnsubscribedCategoryItemProps) {
  const theme = useTheme();

  const { hoveringPosY, handleHoverCategoryItem } = useHoverCategoryItem();

  const { data } = useGetSingleCategory({
    categoryId: category.id,
    enabled: !!hoveringPosY,
  });

  const body = {
    colorCode: PALETTE[getRandomNumber(0, PALETTE.length)],
  };

  const { mutate } = usePostSubscription({ categoryId: category.id });

  const handleClickSubscribeButton = () => {
    mutate(body);
  };

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
        <Button cssProp={subscribeButton(theme)} onClick={handleClickSubscribeButton}>
          구독
        </Button>
      </div>
      {hoveringPosY !== null && (
        <div css={detailStyle(theme, hoveringPosY < innerHeight / 2)}>
          {`구독자 ${data?.data.subscriberCount ?? '-'}명 • 개설일 ${getISODateString(
            category.createdAt
          )}`}
        </div>
      )}
    </div>
  );
}

export default UnsubscribedCategoryItem;
