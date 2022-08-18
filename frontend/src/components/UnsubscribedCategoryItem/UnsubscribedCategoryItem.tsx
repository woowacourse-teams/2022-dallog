import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import { CategoryType } from '@/@types/category';
import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';

import { CACHE_KEY } from '@/constants/api';
import { PALETTE } from '@/constants/style';

import { getRandomNumber } from '@/utils';
import { getISODateString } from '@/utils/date';

import subscriptionApi from '@/api/subscription';

import { categoryItem, item, subscribeButton } from './UnsubscribedCategoryItem.styles';

interface UnsubscribedCategoryItemProps {
  category: CategoryType;
}

function UnsubscribedCategoryItem({ category }: UnsubscribedCategoryItemProps) {
  const { accessToken } = useRecoilValue(userState);

  const theme = useTheme();

  const body = {
    colorCode: PALETTE[getRandomNumber(0, PALETTE.length)],
  };

  const queryClient = useQueryClient();
  const { mutate } = useMutation<
    AxiosResponse<Pick<SubscriptionType, 'colorCode'>>,
    AxiosError,
    Pick<SubscriptionType, 'colorCode'>,
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
      <span css={item}>{getISODateString(category.createdAt)}</span>
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
