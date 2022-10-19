import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import { SubscriptionType } from '@/@types/subscription';

import { userState } from '@/recoil/atoms';

import { CACHE_KEY } from '@/constants/api';

import subscriptionApi from '@/api/subscription';

interface UseDeleteSubscriptionParams {
  subscriptionId: number;
  onSuccess?: () => void;
}

interface UseGetSubscriptionsParams {
  enabled?: boolean;
}

interface UsePatchSubscriptionParams {
  subscriptionId: number;
  onSuccess?: () => void;
}

interface UsePostSubscriptionParams {
  categoryId: number;
  onSuccess?: () => void;
}

function useDeleteSubscriptions({ subscriptionId, onSuccess }: UseDeleteSubscriptionParams) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();

  const { mutate } = useMutation(() => subscriptionApi.delete(accessToken, subscriptionId), {
    onSuccess: () => {
      queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
      queryClient.invalidateQueries(CACHE_KEY.SCHEDULES);
      queryClient.invalidateQueries(CACHE_KEY.CATEGORY);

      onSuccess && onSuccess();
    },
  });

  return { mutate };
}

function useGetSubscriptions({ enabled }: UseGetSubscriptionsParams) {
  const { accessToken } = useRecoilValue(userState);

  const { isLoading, error, data } = useQuery<AxiosResponse<SubscriptionType[]>, AxiosError>(
    CACHE_KEY.SUBSCRIPTIONS,
    () => subscriptionApi.get(accessToken),
    {
      enabled,
    }
  );

  return { isLoading, error, data };
}

function usePatchSubscription({ subscriptionId, onSuccess }: UsePatchSubscriptionParams) {
  const { accessToken } = useRecoilValue(userState);
  const queryClient = useQueryClient();

  const { isLoading, mutate } = useMutation(
    (body: Pick<SubscriptionType, 'colorCode'> | Pick<SubscriptionType, 'checked'>) =>
      subscriptionApi.patch(accessToken, subscriptionId, body),
    {
      onSuccess: async () => {
        await queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
        await queryClient.invalidateQueries(CACHE_KEY.SCHEDULES);

        onSuccess && onSuccess();
      },
    }
  );

  return { isLoading, mutate };
}

function usePostSubscription({ categoryId, onSuccess }: UsePostSubscriptionParams) {
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
      queryClient.invalidateQueries(CACHE_KEY.SCHEDULES);
      queryClient.invalidateQueries([CACHE_KEY.CATEGORY, categoryId]);

      onSuccess && onSuccess();
    },
  });

  return { mutate };
}

export { useDeleteSubscriptions, useGetSubscriptions, usePatchSubscription, usePostSubscription };
