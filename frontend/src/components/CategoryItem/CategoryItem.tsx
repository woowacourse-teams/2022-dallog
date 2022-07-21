import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { QueryObserverResult, RefetchOptions, RefetchQueryFilters, useMutation } from 'react-query';
import { useRecoilValue } from 'recoil';

import { CategoryType } from '@/@types/category';
import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/atoms';

import SubscribeButton from '@/components/SubscribeButton/SubscribeButton';

import { CONFIRM_MESSAGE } from '@/constants';

import subscriptionApi from '@/api/subscription';

import { categoryItem, item } from './CategoryItem.styles';

interface CategoryItemProps {
  category: CategoryType;
  isSubscribing: boolean;
  refetchSubscriptions: <T>(
    options?: (RefetchOptions & RefetchQueryFilters<T>) | undefined
  ) => Promise<QueryObserverResult<AxiosResponse<SubscriptionType[]>, AxiosError>>;
}

function CategoryItem({ category, isSubscribing, refetchSubscriptions }: CategoryItemProps) {
  const theme = useTheme();

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
      refetchSubscriptions();
    },
  });

  const { mutate: deleteSubscription } = useMutation(
    () => subscriptionApi.delete(accessToken, category.id),
    {
      onSuccess: () => {
        refetchSubscriptions();
      },
    }
  );

  const unsubscribe = () => {
    if (window.confirm(CONFIRM_MESSAGE.UNSUBSCRIBE)) {
      deleteSubscription();
    }
  };

  const handleClickSubscribeButton = () => {
    isSubscribing ? unsubscribe() : postSubscription(body);
  };

  return (
    <div css={categoryItem(theme)}>
      <span css={item}>{category.createdAt.split('T')[0]}</span>
      <span css={item}>{category.name}</span>
      <div css={item}>
        <SubscribeButton
          isSubscribing={isSubscribing}
          handleClickSubscribeButton={handleClickSubscribeButton}
        ></SubscribeButton>
      </div>
    </div>
  );
}

export default CategoryItem;
