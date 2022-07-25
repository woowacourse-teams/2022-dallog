import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import { CategoryType } from '@/@types/category';
import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import SubscribeButton from '@/components/SubscribeButton/SubscribeButton';

import { CACHE_KEY, CONFIRM_MESSAGE } from '@/constants';

import subscriptionApi from '@/api/subscription';

import { categoryItem, item } from './CategoryItem.styles';

interface CategoryItemProps {
  category: CategoryType;
  subscriptionId: number;
}

function CategoryItem({ category, subscriptionId }: CategoryItemProps) {
  const theme = useTheme();
  const queryClient = useQueryClient();

  const { accessToken } = useRecoilValue(userState);

  const body = {
    color: '#ffffff',
  };

  const { mutate: postSubscription } = useMutation<
    AxiosResponse<Pick<SubscriptionType, 'color'>>,
    AxiosError,
    Pick<SubscriptionType, 'color'>,
    unknown
  >(() => subscriptionApi.post(accessToken, category.id, body), {
    onSuccess: () => {
      queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
    },
  });

  const { mutate: deleteSubscription } = useMutation(
    () => subscriptionApi.delete(accessToken, subscriptionId),
    {
      onSuccess: () => {
        queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
      },
    }
  );

  const unsubscribe = () => {
    if (window.confirm(CONFIRM_MESSAGE.UNSUBSCRIBE)) {
      deleteSubscription();
    }
  };

  const handleClickSubscribeButton = () => {
    subscriptionId > 0 ? unsubscribe() : postSubscription(body);
  };

  return (
    <div css={categoryItem(theme)}>
      <span css={item}>{category.createdAt.split('T')[0]}</span>
      <span css={item}>{category.name}</span>
      <div css={item}>
        <SubscribeButton
          isSubscribing={subscriptionId > 0}
          handleClickSubscribeButton={handleClickSubscribeButton}
        ></SubscribeButton>
      </div>
    </div>
  );
}

export default CategoryItem;
