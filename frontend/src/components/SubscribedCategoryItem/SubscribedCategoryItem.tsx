import { useTheme } from '@emotion/react';
import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useUserValue from '@/hooks/useUserValue';

import { CategoryType } from '@/@types/category';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';

import { CACHE_KEY } from '@/constants/api';
import { CONFIRM_MESSAGE, TOOLTIP_MESSAGE } from '@/constants/message';

import { getISODateString } from '@/utils/date';

import subscriptionApi from '@/api/subscription';

import { categoryItem, item, menuTitle, unsubscribeButton } from './SubscribedCategoryItem.styles';

interface SubscribedCategoryItemProps {
  category: CategoryType;
  subscriptionId: number;
}

function SubscribedCategoryItem({ category, subscriptionId }: SubscribedCategoryItemProps) {
  const { accessToken } = useRecoilValue(userState);
  const theme = useTheme();

  const queryClient = useQueryClient();

  const { user } = useUserValue();

  const { mutate } = useMutation(() => subscriptionApi.delete(accessToken, subscriptionId), {
    onSuccess: () => {
      queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
    },
  });

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
