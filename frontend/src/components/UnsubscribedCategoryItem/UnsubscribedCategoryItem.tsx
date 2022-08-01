import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import { CategoryType } from '@/@types/category';
import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';

import { CACHE_KEY, PALETTE } from '@/constants';

import { getRandomNumber } from '@/utils';

import subscriptionApi from '@/api/subscription';

import { categoryItem, item, subscribeButton } from './UnsubscribedCategoryItem.styles';

interface UnsubscribedCategoryItemProps {
  category: CategoryType;
}

function UnsubscribedCategoryItem({ category }: UnsubscribedCategoryItemProps) {
  const theme = useTheme();

  const { accessToken } = useRecoilValue(userState);

  const body = {
    color: PALETTE[getRandomNumber(0, PALETTE.length - 1)],
  };

  const queryClient = useQueryClient();
  const { mutate } = useMutation<
    AxiosResponse<Pick<SubscriptionType, 'color'>>,
    AxiosError,
    Pick<SubscriptionType, 'color'>,
    unknown
  >(() => subscriptionApi.post(accessToken, category.id, body), {
    onSuccess: () => {
      queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
    },
  });

  const handleClickSubscribeButton = () => {
    mutate(body);
  };

  return (
    <div css={categoryItem}>
      <span css={item}>{category.createdAt.split('T')[0]}</span>
      <span css={item}>{category.name}</span>
      <div css={item}>
        <Button cssProp={subscribeButton(theme)} onClick={handleClickSubscribeButton}>
          구독
        </Button>
      </div>
    </div>
  );
}

export default UnsubscribedCategoryItem;
