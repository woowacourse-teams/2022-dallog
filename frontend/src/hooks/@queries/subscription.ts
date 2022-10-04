import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import { CACHE_KEY } from '@/constants/api';

import subscriptionApi from '@/api/subscription';

interface useDeleteSubscriptionProps {
  subscriptionId: number;
  onSuccess?: () => void;
}

interface useGetSubscriptionsProps {
  enabled?: boolean;
}

interface usePatchSubscriptionProps {
  subscriptionId: number;
  onSuccess?: () => void;
}

interface usePostSubscriptionProps {
  categoryId: number;
  onSuccess?: () => void;
}

function useDeleteSubscriptions({ subscriptionId, onSuccess }: useDeleteSubscriptionProps) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();

  const { mutate } = useMutation(() => subscriptionApi.delete(accessToken, subscriptionId), {
    onSuccess: () => {
      queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
      onSuccess && onSuccess();
    },
  });

  return { mutate };
}

function useGetSubscriptions({ enabled }: useGetSubscriptionsProps) {
  const { accessToken } = useRecoilValue(userState);

  const { isLoading, error, data } = useQuery<AxiosResponse<SubscriptionType[]>, AxiosError>(
    [CACHE_KEY.SUBSCRIPTIONS],
    () => subscriptionApi.get(accessToken),
    {
      enabled,
    }
  );

  return { isLoading, error, data };
}

function usePatchSubscription({ subscriptionId, onSuccess }: usePatchSubscriptionProps) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();

  const { isLoading, mutate } = useMutation(
    (body: Pick<SubscriptionType, 'colorCode'> | Pick<SubscriptionType, 'checked'>) =>
      subscriptionApi.patch(accessToken, subscriptionId, body),
    {
      onSuccess: () => {
        queryClient.invalidateQueries();
        onSuccess && onSuccess();
      },
    }
  );

  return { isLoading, mutate };
}

function usePostSubscription({ categoryId, onSuccess }: usePostSubscriptionProps) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();

  const { mutate } = useMutation<
    AxiosResponse<Pick<SubscriptionType, 'colorCode'>>,
    AxiosError,
    Pick<SubscriptionType, 'colorCode'>,
    unknown
  >((body) => subscriptionApi.post(accessToken, categoryId, body), {
    onSuccess: () => {
      queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
      onSuccess && onSuccess();
    },
  });

  return { mutate };
}

export { useDeleteSubscriptions, useGetSubscriptions, usePatchSubscription, usePostSubscription };
