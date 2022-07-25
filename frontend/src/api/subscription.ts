import { AxiosResponse } from 'axios';

import { SubscriptionType } from '@/@types/subscription';

import dallogApi from './';

const subscriptionApi = {
  endpoint: {
    get: '/api/members/me/subscriptions',
    post: (categoryId: number) => `/api/members/me/categories/${categoryId}/subscriptions`,
    delete: (subscriptionId: number) => `/api/members/me/subscriptions/${subscriptionId}`,
  },

  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json',
  },

  get: async (accessToken: string | null) => {
    const response = await dallogApi.get<SubscriptionType[]>(subscriptionApi.endpoint.get, {
      headers: { ...subscriptionApi.headers, Authorization: `Bearer ${accessToken}` },
      transformResponse: (res) => {
        return JSON.parse(res).subscriptions;
      },
    });

    return response;
  },

  post: async (
    accessToken: string | null,
    categoryId: number,
    body: Pick<SubscriptionType, 'color'>
  ) => {
    const response = await dallogApi.post(subscriptionApi.endpoint.post(categoryId), body, {
      headers: { ...subscriptionApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },

  delete: async (
    accessToken: string | null,
    subscriptionId: number
  ): Promise<AxiosResponse<null>> => {
    const response = await dallogApi.delete<null>(subscriptionApi.endpoint.delete(subscriptionId), {
      headers: { ...subscriptionApi.headers, Authorization: `Bearer ${accessToken}` },
    });

    return response;
  },
};

export default subscriptionApi;
