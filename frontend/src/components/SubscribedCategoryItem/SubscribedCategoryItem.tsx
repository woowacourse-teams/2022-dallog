import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import { CategoryType } from '@/@types/category';
import { ProfileType } from '@/@types/profile';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';

import { CACHE_KEY } from '@/constants';
import { CONFIRM_MESSAGE, TOOLTIP_MESSAGE } from '@/constants/message';

import { getISODateString } from '@/utils/date';

import profileApi from '@/api/profile';
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

  const { data } = useQuery<AxiosResponse<ProfileType>, AxiosError>(CACHE_KEY.PROFILE, () =>
    profileApi.get(accessToken)
  );

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

  const canUnsubscribeCategory = category.creator.id !== data?.data.id;

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
